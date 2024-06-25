package com.heroku.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/milkProductionReport")
public class ReportController {

    // This method serves the initial HTML page for the Milk Production Report
    @GetMapping("/index")
    public String getIndexPage(Model model) {
        // Add any initial data to the model if needed
        return "index";
    }

    // This method handles the POST request to generate the production report
    @PostMapping("/generateReport")
    public String generateReport(@RequestParam("fromDate") String fromDate,
                                 @RequestParam("toDate") String toDate,
                                 Model model) {
        // Implement your logic here to generate the report based on fromDate and toDate
        // For now, let's assume we return a static report
        List<ProductionReport> reports = generateSampleReport();

        // Add the generated report to the model
        model.addAttribute("reports", reports);

        return "index"; // Return to the index page with the report
    }

    // Example method to generate a sample report (replace with your actual logic)
    private List<ProductionReport> generateSampleReport() {
        List<ProductionReport> reports = new ArrayList<>();
        reports.add(new ProductionReport(1, "2024-06-25 08:00", "E123", 500, 1000, 500));
        reports.add(new ProductionReport(2, "2024-06-24 10:00", "E124", 600, 1200, 600));
        return reports;
    }

    // Example class to represent a production report entry
    private static class ProductionReport {
        private int productionId;
        private String productionTime;
        private String employeeId;
        private int amountIn;
        private int totalProduction;
        private int balance;

        public ProductionReport(int productionId, String productionTime, String employeeId, int amountIn, int totalProduction, int balance) {
            this.productionId = productionId;
            this.productionTime = productionTime;
            this.employeeId = employeeId;
            this.amountIn = amountIn;
            this.totalProduction = totalProduction;
            this.balance = balance;
        }

        public int getProductionId() {
            return productionId;
        }

        public String getProductionTime() {
            return productionTime;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public int getAmountIn() {
            return amountIn;
        }

        public int getTotalProduction() {
            return totalProduction;
        }

        public int getBalance() {
            return balance;
        }
    }
}
