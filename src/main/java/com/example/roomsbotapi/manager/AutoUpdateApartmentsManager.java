package com.example.roomsbotapi.manager;

import com.example.roomsbotapi.models.Apartments.Apartments;
import com.example.roomsbotapi.models.Apartments.pojos.*;
import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.ApartmentsService;
import com.example.roomsbotapi.services.UserService;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@NoArgsConstructor
public class AutoUpdateApartmentsManager {

    private  ApartmentsService apartmentsService;
    private  UserService userService;

    @Autowired
    public AutoUpdateApartmentsManager(ApartmentsService apartmentsService, UserService userService) {
        this.apartmentsService = apartmentsService;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 3000000)
    @Async
    public void apiParsingXml() {

        //Kiev
        log.info("Parsing Kiev");
        urlParser("https://v3api.citybase.com.ua/xml?city=Kyiv&section=rent_living&company=380935177996&published_in_days=5");
        urlParser("https://v3api.citybase.com.ua/xml?city=Kyiv&section=sale_living&company=380935177996&published_in_days=5");


        //Odessa
        log.info("Parsing Odessa");
        urlParser("https://v3api.citybase.com.ua/xml?city=Odessa&section=rent_living&company=380935177996&published_in_days=5");
        urlParser("https://v3api.citybase.com.ua/xml?city=Odessa&section=sale_living&company=380935177996&published_in_days=5");

        //Kharkov
        log.info("Parsing Kharkov");
        urlParser("https://v3api.citybase.com.ua/xml?city=Kharkov&section=sale_living&company=380935177996&published_in_days=5");
        urlParser("https://v3api.citybase.com.ua/xml?city=Kharkov&section=rent_living&company=380935177996&published_in_days=5");
    }


    @SneakyThrows
    @Scheduled(fixedDelay = 86400000, initialDelay = 15000)
    public void deleteOldApartments() {
        List<Apartments> apartmentsList = apartmentsService.findAll();
        List<User> users = userService.findAll();

        apartmentsList.forEach(apartment ->  {
            LocalDate localDateLastUpdate = LocalDate.parse(apartment.getLastUpdateDate());
            int days = Days.daysBetween(localDateLastUpdate, LocalDate.now()).getDays();

            if (days >= 6) {
                System.out.println(days + " - days\n" + apartment.getLastUpdateDate() + " - deleted");
                apartmentsService.deleteByInternalId(apartment.getInternalId());
            }
        });

        log.info("today compilation start...");
        users.forEach(item -> userService.todayCompilationUser(item));
        log.info("today compilation end");

        userService.saveAll(users);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "GMT+3")
    public void todayCompilation() {
        List<User> users = userService.findAll();
        for (User user : users) {
            userService.todayCompilationUser(user);
        }

        userService.saveAll(users);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "GMT+3")
    public void updateDaysOfSubscription() {
        List<User> allUsers = userService.findAll();

        for (User user : allUsers) {
            if (user.getDaysOfSubscription() == 0)
                continue;

            if (user.getDaysOfSubscription() > 0) {
                user.setDaysOfSubscription(user.getDaysOfSubscription() - 1);
            }

            userService.save(user);
        }
    }



    private void urlParser(String urlString) {
        try {

            int countSaveData = 0, countContinueData = 0;
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();

            try {
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = db.parse(new ByteArrayInputStream(content.toString().getBytes(StandardCharsets.UTF_8)));

                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("offer");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Apartments apartments = new Apartments();
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        Long internalId = Long.parseLong(eElement.getAttribute("internal-id"));
                        apartments.setInternalId(internalId);
                        apartments.setCreationDate(eElement.getElementsByTagName("creation-date").item(0).getTextContent().split("T")[0]);
                        apartments.setLastUpdateDate(eElement.getElementsByTagName("last-update-date").item(0).getTextContent().split("T")[0]);
                        apartments.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
                        apartments.setPropertyType(eElement.getElementsByTagName("property-type").item(0).getTextContent());
                        apartments.setCategory(eElement.getElementsByTagName("category").item(0).getTextContent());
                        try {
                            apartments.setRooms(Integer.parseInt(eElement.getElementsByTagName("rooms").item(0).getTextContent()));
                        } catch (Exception e) {
                            apartments.setRooms(null);
                        }

                        try {
                            apartments.setFloor(Integer.parseInt(eElement.getElementsByTagName("floor").item(0).getTextContent()));
                        } catch (Exception e) {
                            apartments.setFloor(null);
                        }
                        try {
                            apartments.setFloorsTotal(Integer.parseInt(eElement.getElementsByTagName("floors-total").item(0).getTextContent()));
                        } catch (Exception e) {
                            apartments.setFloorsTotal(null);
                        }

                        apartments.setUrl(eElement.getElementsByTagName("url").item(0).getTextContent());
                        apartments.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());

                        //parse and set images
                        List<String> imagesList = new ArrayList<>();
                        NodeList images = eElement.getElementsByTagName("image");
                        for (int i = 0; i < images.getLength(); i++) {
                            imagesList.add(images.item(i).getTextContent());
                        }
                        apartments.setImages(imagesList);

                        //parse and set area
                        String value = "", unit = "";
                        try {
                            value = ((Element) eElement.getElementsByTagName("area").item(0)).getElementsByTagName("value").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            unit = ((Element) eElement.getElementsByTagName("area").item(0)).getElementsByTagName("unit").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }

                        Area area = new Area(value, unit);
                        apartments.setArea(area);

                        //parse and set price
                        long valuePrice = 0L;
                        String currency = "";
                        try {
                            valuePrice = Long.parseLong(((Element) eElement.getElementsByTagName("price").item(0)).getElementsByTagName("value").item(0).getTextContent());
                        } catch (Exception ignored) {
                        }
                        try {
                            currency = ((Element) eElement.getElementsByTagName("price").item(0)).getElementsByTagName("currency").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }

                        Price price = new Price(valuePrice, currency);
                        apartments.setPrice(price);

                        //parse and set location
                        String country = "", region = "", locationName = "", subLocationName = "", nonAdminSubLocality = "",
                                address = "", house = "", nameStation, distance;
                        try {
                            country = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("country").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            region = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("region").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            locationName = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("locality-name").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            subLocationName = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("sub-locality-name").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            nonAdminSubLocality = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("non-admin-sub-locality").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            address = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("address").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        try {
                            house = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("house").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }

                        try {
                            Node item = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("metro").item(0);
                            nameStation = ((Element) item).getElementsByTagName("name").item(0).getTextContent();
                        } catch (Exception e) {
                            nameStation = "";
                        }

                        try {
                            Node item = ((Element) eElement.getElementsByTagName("location").item(0)).getElementsByTagName("metro").item(0);
                            distance = ((Element) item).getElementsByTagName("distance").item(0).getTextContent();
                        } catch (Exception e) {
                            distance = "";
                        }

                        Location location = new Location(country, region, locationName, subLocationName, nonAdminSubLocality, address, house, new Metro(nameStation, distance));
                        apartments.setLocation(location);

                        //parse and set sales-agent
                        String phone = "";
                        try {
                            phone = ((Element) eElement.getElementsByTagName("sales-agent").item(0)).getElementsByTagName("phone").item(0).getTextContent();
                        } catch (Exception ignored) {
                        }
                        SalesAgent salesAgent = new SalesAgent(phone);
                        apartments.setSalesAgent(salesAgent);

                        try {
                            var savedApartments = apartmentsService.findByInternalId(internalId);
                            if (savedApartments != null) {
                                countContinueData += 1;
                            } else {
                                countSaveData += 1;
                                apartmentsService.save(apartments);
                            }
                        } catch (Exception e) {
                            countSaveData += 1;
                            apartmentsService.save(apartments);
                        }
                    }
                }

                log.info("[Saved: " + countSaveData + " Continue: " + countContinueData + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("network error");
        }
    }


}