/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esale.frontend.controller;

import com.google.gson.Gson;
import esale.frontend.common.Utils;
import esale.frontend.entity.EntityExample;
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
public class APIController extends HttpServlet {

    private static Logger logger = Logger.getLogger(APIController.class);
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if(id != null){
                EntityExample user = new EntityExample();
                user.setAddress("Ho Chi Minh");
                user.setPhoneNumber("0909123456");
                user.setUserName("Nguyen Van A");
                String output = gson.toJson(user);
                Utils.out(output, resp);
            }
            else{
                Utils.out("ID user ko dc rong", resp);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
