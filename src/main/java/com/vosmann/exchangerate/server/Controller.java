package com.vosmann.exchangerate.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vosmann.exchangerate.Rate;
import com.vosmann.exchangerate.RateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;
import java.util.List;

import static com.vosmann.exchangerate.utils.RateDates.isValidPeriod;

/**
 * Delivers the EUR-to-USD exchange rates. Also sanitizes input and serializes responses.
 */
@RestController
public class Controller {

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    private static final String SELECT_LATEST = "latest";
    private static final String SELECT_RANGE = "range";

    private static final String EMPTY_RESPONSE = "[]";
    private static final String ERROR_RESPONSE = "Incorrect GET call. Use either " +
            "'/rates?select=latest' or " +
            "'/rates?select=range&start_date=2017-09-23&start_date=2017-09-29'.";

    @Autowired
    private RateService rateService;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/")
    @ResponseBody
    String root() {
        LOG.info("Received request at /.");
        return ERROR_RESPONSE;
    }

    @RequestMapping("/rates")
    @ResponseBody
    String getRates(@RequestParam String select,
                    @RequestParam(value = "start_date", required = false) String start,
                    @RequestParam(value = "end_date", required = false) String end) {
        LOG.info("Received request at /rates.");

        switch (select) {
            case SELECT_LATEST:
                return retrieveLastRate();
            case SELECT_RANGE:
                return retrieveRangeRate(start, end);
            default:
                return ERROR_RESPONSE;
        }
    }

    private String retrieveLastRate() {
        try {
            final List<Rate> rate = rateService.getLatest();
            final String response = objectMapper.writeValueAsString(rate);
            return response;
        } catch (JsonProcessingException e) {
            LOG.error("Could not produce latest rate.", e);
            return EMPTY_RESPONSE;
        }
    }

    private String retrieveRangeRate(String startDate, String endDate) {
        if (!isValidPeriod(startDate, endDate)) {
            LOG.error("Invalid dates. startDate: {} endDate: {}.", startDate, endDate);
            return EMPTY_RESPONSE + "; Invalid dates.";
        }

        try {
            final List<Rate> rates = rateService.getFrom(startDate, endDate);
            final String response = objectMapper.writeValueAsString(rates);

            return response;
        } catch (DateTimeParseException | JsonProcessingException e) {
            LOG.error("Could not produce rates for range from {} to {}.", startDate, endDate, e);
            return EMPTY_RESPONSE;
        }
    }

}

