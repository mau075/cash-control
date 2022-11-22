<%-- 
    Document   : cuadreDiario
    Created on : 27-10-2018, 02:49:06 PM
    Author     : Felipe Mauricio Gonzales Subirana
--%>
<%@page import="model.Agency"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@page import="net.sf.jasperreports.export.SimpleOutputStreamExporterOutput"%>
<%@page import="net.sf.jasperreports.export.SimpleExporterInput"%>
<%@page import="net.sf.jasperreports.engine.export.JRPdfExporter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="application/pdf"%>
<%@page trimDirectiveWhitespaces = "true" %>

<%@page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="net.sf.jasperreports.engine.JasperReport"%>
<%@page import="net.sf.jasperreports.engine.JasperCompileManager"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>

<%
    Connection connection = null;

    HashMap hm = new HashMap();
    Date created = (Date)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Created");
    Agency agency = (Agency)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Agency");
    hm.put("created", created);
    hm.put("idAgency", agency.getIdAgency());
    hm.put("idBank", "BUN");
    hm.put("mainVault", "main");
    
    try{
        String jrxmlFile;
        InputStream input;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://:3306/cashcontrol", "root", "admin123");

        jrxmlFile = session.getServletContext().getRealPath("/reports/cuadreDiario.jrxml");
        input = new FileInputStream(new File(jrxmlFile));
        jasperReport = JasperCompileManager.compileReport(input);
        jasperPrint = JasperFillManager.fillReport(jasperReport, hm, connection);

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        response.getOutputStream().flush();
        response.getOutputStream().close();
        
    } catch(Exception e){
        e.printStackTrace();
    }finally{
        if (connection != null)
            connection.close();
    }
%>
