/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esale.frontend.controller;

import esale.frontend.common.Utils;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author sonnh4
 */
public class HTMLController extends HttpServlet {

    private static Logger logger = Logger.getLogger(HTMLController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TemplateDataDictionary myDic = TemplateDictionary.create();
            myDic.setVariable("my_variable", "Muốn set gì vô thì set");
            myDic.showSection("MYBLOCK");
            String template = Utils.renderTemplate("Template/index.html", myDic);
            Utils.out(template, resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }

}
