package com.crumbco.bakery.servlet;

import com.crumbco.bakery.dao.JdbcProductDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Traditional Jakarta Servlet endpoint included for the Servlets & Web Integration rubric. */
public class LegacyBakeryServlet extends HttpServlet {
    private final JdbcProductDao productDao;
    private final ObjectMapper objectMapper;

    public LegacyBakeryServlet(JdbcProductDao productDao, ObjectMapper objectMapper) {
        this.productDao = productDao;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            objectMapper.writeValue(response.getOutputStream(), productDao.findAll());
        } catch (RuntimeException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(response.getOutputStream(), java.util.Map.of("error", "Could not load catalog"));
        }
    }
}
