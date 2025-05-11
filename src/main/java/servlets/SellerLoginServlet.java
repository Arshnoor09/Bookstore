package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.db.UsersDBConstants;
import com.bittercode.model.User;
import com.bittercode.model.UserRole;
import com.bittercode.service.UserService;
import com.bittercode.service.impl.UserServiceImpl;

public class SellerLoginServlet extends HttpServlet {

    UserService userService = new UserServiceImpl();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        // Set content type and character encoding
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        res.setCharacterEncoding("UTF-8"); // ✅ Ensures symbols/emojis are rendered properly

        PrintWriter pw = res.getWriter();

        String uName = req.getParameter(UsersDBConstants.COLUMN_USERNAME);
        String pWord = req.getParameter(UsersDBConstants.COLUMN_PASSWORD);

        try {
            User user = userService.login(UserRole.SELLER, uName, pWord, req.getSession());
            if (user != null) {
                pw.println("<!DOCTYPE html>");
                pw.println("<html lang='en'><head>");
                pw.println("<meta charset='UTF-8'>");
                pw.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                pw.println("<title>QuillQuest</title>");
                pw.println("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
                pw.println("<style>");
                pw.println("body::before { content: ''; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-image: url('https://png.pngtree.com/background/20230522/original/pngtree-stack-of-books-on-top-of-a-table-picture-image_2695945.jpg'); background-size: cover; background-position: center; filter: blur(5px); z-index: -1; }");
                pw.println("body { font-family: 'Segoe UI', sans-serif; margin: 0; padding: 0; min-height: 100vh; display: flex; flex-direction: column; position: relative; z-index: 0; }");
                pw.println(".navbar { padding: 0.5rem 1rem; background: #343a40 !important; height: 60px; box-shadow: 0 2px 8px rgba(0,0,0,0.15); }");
                pw.println(".navbar-brand img { height: 40px; width: auto; margin-right: 10px; }");
                pw.println(".store-name { color: #f0f0f0; font-weight: 600; font-size: 18px; }");
                pw.println(".navbar-nav .nav-link { color: white !important; font-weight: 500; margin: 0 1rem; position: relative; font-size: 16px; transition: all 0.3s ease; padding-bottom: 5px; }");
                pw.println(".navbar-nav .nav-link.active { color: #00c6ff !important; }");
                pw.println(".navbar-nav .nav-link::after { content: ''; position: absolute; bottom: 0; left: 50%; width: 0; height: 2px; background-color: #ffffff; transition: all 0.3s ease; transform: translateX(-50%); }");
                pw.println(".navbar-nav .nav-link:hover::after, .navbar-nav .nav-link.active::after { width: 100%; }");
                pw.println(".admin-card { max-width: 700px; margin: 60px auto; background-color: rgba(255, 255, 255, 0.9); padding: 40px; border-radius: 20px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1); text-align: center; }");
                pw.println(".admin-card h1 { font-size: 2.5rem; color: #343a40; margin-bottom: 20px; }");
                pw.println(".admin-card p { font-size: 1.2rem; color: #555; }");
                pw.println(".dashboard-options { background: rgba(255, 255, 255, 0.1); border: 1px solid rgba(255, 255, 255, 0.3); border-radius: 20px; backdrop-filter: blur(12px); padding: 30px; margin: 50px auto; width: 80%; box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2); text-align: center; }");
                pw.println(".dashboard-options a { display: inline-block; margin: 15px; padding: 15px 30px; border-radius: 12px; background: linear-gradient(135deg, #f6d365 0%, #fda085 100%); color: #fff; text-decoration: none; font-size: 18px; font-weight: bold; transition: all 0.3s ease; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2); }");
                pw.println(".dashboard-options a:hover { transform: scale(1.07); box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3); background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }");
                pw.println("footer { margin-top: auto; padding: 20px; text-align: center; color: #999; }");
                pw.println("</style></head><body>");

                pw.println("<header><nav class='navbar navbar-expand-lg navbar-dark'>");
                pw.println("<a class='navbar-brand' href='/onlinebookstore/'><img src='logo.png' alt='Book Store Logo'><span class='store-name'>QuillQuest</span></a>");
                pw.println("<button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav'><span class='navbar-toggler-icon'></span></button>");
                pw.println("<div class='collapse navbar-collapse' id='navbarNav'><ul class='navbar-nav ml-auto'>");
                pw.println("<li class='nav-item'><a class='nav-link active' href='AdminLanding.html'>Home</a></li>");
                pw.println("<li class='nav-item'><a class='nav-link' href='storebooks'>Store Books</a></li>");
                pw.println("<li class='nav-item'><a class='nav-link' href='addbook'>Add Books</a></li>");
                pw.println("<li class='nav-item'><a class='nav-link' href='removebook'>Remove Books</a></li>");
                pw.println("<li class='nav-item'><a class='nav-link' href='about'>About Us</a></li>");
                pw.println("<li class='nav-item'><a class='nav-link' href='logout'>Logout</a></li>");
                pw.println("</ul></div></nav></header>");

                pw.println("<div class='admin-card'>");
                pw.println("<h1>Welcome Admin 👋</h1>");
                pw.println("<p>Manage your bookstore efficiently — add, update, and track your books from here. Let's build a better library together!</p>");
                pw.println("<div class='dashboard-options'>");
                pw.println("<a href='addbook'>➕ Add Book</a>");
                pw.println("<a href='removebook'>❌ Remove Book</a>");
                pw.println("<a href='storebooks'>📚 View All Books</a>");
                pw.println("<a href='about'>ℹ️ About Us</a>");
                pw.println("</div></div>");

                pw.println("<footer>&copy; 2025 QuillQuest. All Rights Reserved.</footer>");
                pw.println("</body></html>");
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Incorrect UserName or PassWord</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
