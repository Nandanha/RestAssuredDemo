package com.io.reporter;

import java.util.List;

public class TableCreator {

    private String fillUpHeadDataForSummaryTable() {
        String tableData = "<table style=\"font-size: 12px;width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "				<thead style=\"width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "					<tr>\r\n" +
                "						<th colspan=\"5\" style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 8px;background-color: #508abb;color: #fff;\">SCENARIOS</th>\r\n" +
                "					</tr>\r\n" +
                "					<tr>";
        for(int i = 0; i < getHeadValuesForSummaryTable().length; i++) {
            tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: left; padding: 8px;background-color: #508abb;color: #fff;\">"+ getHeadValuesForSummaryTable()[i]+"</th>";
        }
        tableData = tableData + "</tr></thead><tbody style=\"font-size: 12px;\">";
        return tableData;
    }

    private String[] getHeadValuesForSummaryTable() {
        String[] headValues = new String[5];
        headValues[0] = "API";
        headValues[1] = "Total";
        headValues[2] = "Passed";
        headValues[3] = "Failed";
        headValues[4] = "Skipped";
        return headValues;
    }

    private String fillUpSuiteData(List<String[]> suiteDetails) {
        StringBuilder tableData = new StringBuilder();
        for (String[] suiteDetail : suiteDetails) {
            tableData.append("<tr style=\"width: 100%;border-bottom:1px solid #efefef;border-top:1px solid #ececec;background-color:#f4fbff;\">");
            for (String s : suiteDetail) {
                tableData.append("<td style=\"border-collapse:collapse;text-align: left; padding: 8px\">").append(s).append("</td>");
            }
            tableData.append("</tr>");
        }
        tableData.append("</tbody>");
        return tableData.toString();
    }

    private String fillUpFootData(List<String[]> projectDetails) {
        String footData = "<tfoot><tr style=\"width: 100%;border-spacing: 2px;background-color:#fcffc9 !important\">";
        for(int i = 0; i < projectDetails.get(0).length; i++) {
            footData = footData + "<td style=\"border-collapse:collapse;text-align: left; padding: 8px\">" + projectDetails.get(0)[i] + "</td>";
        }
        footData = footData + "</tr></tfoot></table>";
        return footData;
    }

    public String generateFeatureTable(List<String[]> suiteDetails, List<String[]> projectDetails) {
        return (fillUpHeadDataForSummaryTable() + fillUpSuiteData(suiteDetails) + fillUpFootData(projectDetails));
    }

    private String fillUpHeadDataForFailureTable() {
        String tableData = "</blockquote><h4>FAILURE SUMMARY</h4><blockquote><table style=\"font-size: 12px;width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "				<thead style=\"width: 100%;border-spacing: 2px;border-color:grey\">\r\n" +
                "					<tr>\r\n" +
                "						<th colspan=\"5\" style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: center; padding: 8px;background-color: #508abb;color: #fff;\">SCENARIOS</th>\r\n" +
                "					</tr>\r\n" +
                "					<tr>";
        for(int i = 0; i < getHeadValuesForFailureTable().length; i++) {
            tableData = tableData + "<th style=\"font-size: 14px;border: 1px #6ea1cc !important;text-align: left; padding: 8px;background-color: #508abb;color: #fff;\">"+getHeadValuesForFailureTable()[i]+"</th>";
        }
        tableData = tableData + "</tr></thead><tbody style=\"font-size: 12px;\">";
        return tableData;
    }

    private String[] getHeadValuesForFailureTable() {
        String[] headValues = new String[2];
        headValues[0] = "Method";
        headValues[1] = "Failure Information";
        return headValues;
    }

    private String fillUpFailureData(List<String[]> suiteFailureDetails) {
        String tableData = "";
        for (String[] suiteFailureDetail : suiteFailureDetails) {
            tableData = tableData + "<tr style=\"width: 100%;border-bottom:1px solid #efefef;border-top:1px solid #ececec;background-color:#f4fbff;\">";
            for (String s : suiteFailureDetail) {
                tableData = tableData + "<td style=\"border-collapse:collapse;text-align: left; padding: 8px\">" + s + "</td>";
            }
            tableData = tableData + "</tr>";
        }
        tableData = tableData + "</tbody></table>";
        return tableData;
    }

    public String generateFailureTable(List<String[]> suiteFailureDetails) {
        String tableData = fillUpHeadDataForFailureTable() + fillUpFailureData(suiteFailureDetails);
        return tableData;
    }

}
