package ru.otus.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.ftl";
    private static final String TEMPLATE_CLIENTS = "clients";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = dbServiceClient.findAll();
        paramsMap.put(TEMPLATE_CLIENTS, clients);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Client client = buildClient(req);

        dbServiceClient.saveClient(client);

        response.sendRedirect("/clients");
    }

    private Client buildClient(HttpServletRequest req) {
        Client client = new Client();
        client.setName(req.getParameter("name"));
        Address address = new Address();
        address.setStreet(req.getParameter("street"));
        client.setAddress(address);
        Phone phone = new Phone();
        phone.setNumber(req.getParameter("phone"));
        client.setPhones(Collections.singletonList(phone));
        return client;
    }
}
