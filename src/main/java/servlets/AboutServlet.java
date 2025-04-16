package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.model.UserRole;
import com.bittercode.util.StoreUtil;

public class AboutServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");
        String style = "<style>"
                + "body {"
                + "    margin: 0;"
                + "    font-family: 'Segoe UI', sans-serif;"
                + "    background: url('https://dr2r4w0s7b8qm.cloudfront.net/image_manager_app/24_Seven_Storage_December_2023_2-20231213-021221.jpg') no-repeat center center fixed;"
                + "    background-size: cover;"
                + "}"
                + ".about-overlay {"
                + "    background-color: rgba(255, 255, 255, 0.9);"
                + "    margin: 60px auto;"
                + "    max-width: 1100px;"
                + "    padding: 40px 60px;"
                + "    border-radius: 20px;"
                + "    box-shadow: 0 8px 25px rgba(0,0,0,0.2);"
                + "    display: flex;"
                + "    flex-direction: column;"
                + "    align-items: center;"
                + "}"
                + ".about-text {"
                + "    text-align: center;"
                + "}"
                + ".about-text h1 {"
                + "    font-size: 36px;"
                + "    color: #2c3e50;"
                + "    margin-bottom: 20px;"
                + "}"
                + ".about-text p {"
                + "    font-size: 16px;"
                + "    color: #555;"
                + "    line-height: 1.8;"
                + "}"
                + ".about-btn {"
                + "    background-color: #e67e22;"
                + "    color: white;"
                + "    padding: 12px 24px;"
                + "    text-decoration: none;"
                + "    border-radius: 8px;"
                + "    margin-top: 20px;"
                + "    display: inline-block;"
                + "    font-weight: bold;"
                + "}"
                + ".book-gallery {"
                + "    display: flex;"
                + "    justify-content: center;"
                + "    margin-top: 30px;"
                + "    gap: 30px;"
                + "    flex-wrap: wrap;"
                + "}"
                + ".book-gallery img {"
                + "    width: 160px;"
                + "    height: 240px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);"
                + "    transition: transform 0.3s;"
                + "}"
                + ".book-gallery img:hover {"
                + "    transform: scale(1.05);"
                + "}"
                + ".contact-info {"
                + "    margin-top: 50px;"
                + "    padding: 30px;"
                + "    background-color: #f9f9f9;"
                + "    border-radius: 12px;"
                + "    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);"
                + "    text-align: center;"
                + "    max-width: 600px;"
                + "    margin-left: auto;"
                + "    margin-right: auto;"
                + "    font-size: 16px;"
                + "    line-height: 1.8;"
                + "    color: #2c3e50;"
                + "    font-weight: 500;"
                + "}"
                + ".contact-info h3 {"
                + "    font-size: 20px;"
                + "    margin-bottom: 10px;"
                + "    color: #e67e22;"
                + "}"
                + ".contact-info a {"
                + "    color: #2980b9;"
                + "    text-decoration: none;"
                + "    font-weight: bold;"
                + "}"
                + ".contact-info a:hover {"
                + "    text-decoration: underline;"
                + "}"
                + "</style>";

        if (StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "about");

            pw.println(style); 
            pw.println("<div class='about-overlay'>"
                    + "<div class='about-text'>"
                    + "<h1>Discover Your Next Favorite Book</h1>"
                    + "<p>Welcome to our Online Bookstore. Your digital destination for stories, knowledge, and inspiration.<br>"
                    + "Whether you're a fan of mystery, romance, fantasy, or biographies, we have something for everyone. "
                    + "Customers can purchase books directly and sellers/admins can manage listings with ease.</p>"
                    + "<a class='about-btn' href=\"viewbook\">Browse Our Collection</a>"
                    + "</div>"

                    + "<div class='book-gallery'>"
                    + "<img src='https://sp.yimg.com/ib/th?id=OPAC.q3jERzuieU1TJA474C474&o=5&pid=21.1&w=174&h=174' alt='Book 1' />"
                    + "<img src='https://m.media-amazon.com/images/I/91uwocAMtSL.jpg' alt='Book 2' />"
                    + "<img src='https://m.media-amazon.com/images/I/81vpsIs58WL.jpg' alt='Book 3' />"
                    + "<img src='https://m.media-amazon.com/images/I/71KilybDOoL.jpg' alt='Book 4' />"
                    + "<img src='https://m.media-amazon.com/images/I/710jnzKlDTL.jpg' alt='Book 5' />"
                    + "</div>"

                    + "<div class='contact-info'>"
                    + "<h3>Need Help or Want to Collaborate?</h3>"
                    + "<p>You can contact <strong>Arshnoor</strong> for more information about the bookstore platform.</p>"
                    + "<p>Phone: <a href='tel:9465377757'><i class='bi bi-telephone'>94653 77757</a></p>"
                    + "</div>"
                    + "</div>");

        } else if (StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "about");

            pw.println(style);
            pw.println("<div class='about-overlay'>"
                    + "<div class='about-text'>"
                    + "<h1>Empower the Bookstore Experience</h1>"
                    + "<p>Welcome, Admin! Your dashboard gives you the power to shape the bookstore.<br>"
                    + "Manage book listings, monitor transactions, update inventory, and ensure a smooth reading journey for our users. "
                    + "With you, our readers get the best of literature, always on time and perfectly managed.</p>"
                    + "<a class='about-btn' href=\"addbook\">Add New Book</a>"
                    + "</div>"

                    + "<div class='book-gallery'>"
                    + "<img src='https://m.media-amazon.com/images/I/91uwocAMtSL.jpg' alt='Book A' />"
                    + "<img src='https://m.media-amazon.com/images/I/81vpsIs58WL.jpg' alt='Book B' />"
                    + "<img src='https://m.media-amazon.com/images/I/71KilybDOoL.jpg' alt='Book C' />"
                    + "<img src='https://m.media-amazon.com/images/I/710jnzKlDTL.jpg' alt='Book D' />"
                    + "<img src='https://m.media-amazon.com/images/I/71aFt4+OTOL.jpg' alt='Book E' />"
                    + "</div>"

                    + "<div class='contact-info'>"
                    + "<h3>Need Technical Help or Suggestions?</h3>"
                    + "<p>Connect with <strong>Arshnoor</strong> to keep your admin panel up and running at its best.</p>"
                    + "<p>Phone: <a href='tel:9465377757'><i class='bi bi-telephone'> 94653 77757</a></p>"
                    + "</div>"
                    + "</div>");

        } else {
            RequestDispatcher rd = req.getRequestDispatcher("login.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
        }
    }
}
