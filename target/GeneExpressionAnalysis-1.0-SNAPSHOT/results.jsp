<%@ page import="java.util.List" %>
<%@ page import="GeneData" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Analysis Results</title>
</head>
<body>
    <h1>Analysis Results</h1>
    <h2>Top Differentially Expressed Genes</h2>
    <table border="1">
        <tr>
            <th>Gene</th>
            <th>Fold Change</th>
        </tr>
        <%
            List<GeneData> significantGenes = (List<GeneData>) request.getAttribute("significantGenes");
            for (GeneData gene : significantGenes) {
        %>
            <tr>
                <td><%= gene.getGene() %></td>
                <td><%= gene.getFoldChange() %></td>
            </tr>
        <%
            }
        %>
    </table>
    <h2>Visualization</h2>
    <img src="/static/bar_chart.png" alt="Bar Chart of Top Genes">
</body>
</html>


