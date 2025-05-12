package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.model.UserRole;
import com.bittercode.util.StoreUtil;

public class CheckoutServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }
        try {
            // Including the payment page template
            RequestDispatcher rd = req.getRequestDispatcher("payment.html");
            rd.include(req, res);
            
            // Setting the active tab for the navigation bar
            StoreUtil.setActiveTab(pw, "cart");

            // Displaying the total amount to be paid
            pw.println("<div class=\"container\">");
            pw.println("<h3>Total Amount: <span class=\"price\" style=\"color: black\"><b>&#8377; "
                    + req.getSession().getAttribute("amountToPay") + "</b></span></h3>");

            // Payment button (already in the form in the payment.html)
            // Removed duplicate button print here

            pw.println("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
