
package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.util.StoreUtil;

public class HomeServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        UserRole role = (UserRole) session.getAttribute("role");

        if (user == null || role == null) {
            RequestDispatcher rd = req.getRequestDispatcher("login.html");
            rd.include(req, res);
            pw.println("<div class='tab'>Please Login First to Continue</div>");
            return;
        }

        // Include common navbar (you can use your existing Seller or Customer one)
        RequestDispatcher rd = req.getRequestDispatcher(role == UserRole.SELLER ? "SellerNav.html" : "CustomerNav.html");
        rd.include(req, res);

        String style = "<style>"
            + "body { background: url('https://wallpapercave.com/wp/wp2757874.jpg') no-repeat center center fixed;"
            + " background-size: cover; font-family: 'Segoe UI', sans-serif; color: white; }"
            + ".home-overlay { background-color: rgba(0,0,0,0.7); padding: 50px; border-radius: 20px; max-width: 80%; margin: 50px auto; text-align: center; }"
            + ".home-overlay h1 { font-size: 3em; color: #f39c12; margin-bottom: 20px; }"
            + ".home-overlay p { font-size: 1.4em; }"
            + ".home-overlay .user-role { color: #2ecc71; font-weight: bold; }"
            + "</style>";

        pw.println(style);
        pw.println("<div class='home-overlay'>");
        pw.println("<h1>Welcome to the Quill Quest</h1>");
        pw.println("<p>Hello <strong>" + user.getFirstName() + "</strong>! You are logged in as <span class='user-role'>" + (role == UserRole.SELLER ? "Admin / Seller" : "Customer") + "</span>.</p>");
        pw.println("<p>Enjoy exploring our vast collection of books. " + (role == UserRole.SELLER ? "You can manage listings and view sales stats." : "You can browse and purchase your favorite books.") + "</p>");
        pw.println("</div>");
    }
}
