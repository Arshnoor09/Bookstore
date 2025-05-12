package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.model.Book;
import com.bittercode.model.Cart;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class ViewOrdersServlet extends HttpServlet {

    BookService bookService = new BookServiceImpl();

    @SuppressWarnings("unchecked")
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html;charset=UTF-8");

        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        try {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            StoreUtil.setActiveTab(pw, "orders");

            pw.println("<div id='topmid' class='p-3 mb-3 bg-primary text-white rounded text-center fs-4 fw-bold shadow'>Your Orders</div>");

            HttpSession session = req.getSession();
            Map<String, List<Cart>> orderHistory = (Map<String, List<Cart>>) session.getAttribute("orderHistory");

            if (orderHistory == null || orderHistory.isEmpty()) {
                pw.println("<div class='container my-4'>");
                pw.println("<div class='alert alert-info text-center'>");
                pw.println("<h4>No orders found</h4>");
                pw.println("<p>You haven't placed any orders yet.</p>");
                pw.println("</div></div>");
                return;
            }

            // Display orders grouped by date
            for (Map.Entry<String, List<Cart>> entry : orderHistory.entrySet()) {
                String orderDate = entry.getKey();
                List<Cart> orders = entry.getValue();
                
                // Calculate total amount for this date
                double totalAmount = 0;
                for (Cart cart : orders) {
                    totalAmount += cart.getBook().getPrice() * cart.getQuantity();
                }

                // Display date header and total
                pw.println("<div class='container my-4'>");
                pw.println("<div class='card mb-4'>");
                pw.println("<div class='card-header bg-light'>");
                pw.println("<div class='d-flex justify-content-between align-items-center'>");
                pw.println("<h5 class='mb-0'>Order Date: " + orderDate + "</h5>");
                pw.println("<h5 class='mb-0 text-success'>Total Amount: &#8377;" + String.format("%.2f", totalAmount) + "</h5>");
                pw.println("</div></div>");
                pw.println("<div class='card-body'>");
                pw.println("<div class='row row-cols-1 row-cols-md-2 g-4'>");

                // Display individual orders
                for (Cart cart : orders) {
                    Book book = cart.getBook();
                    double bPrice = book.getPrice();
                    String bCode = book.getBarcode();
                    String bName = book.getName();
                    String bAuthor = book.getAuthor();
                    int availableQty = book.getQuantity();
                    int qtToBuy = cart.getQuantity();
                    pw.println(this.addBookToCard(bCode, bName, bAuthor, bPrice, qtToBuy, availableQty));
                }

                pw.println("</div></div></div></div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addBookToCard(String bCode, String bName, String bAuthor, double bPrice, int quantity, int bQty) {
        return "<div class=\"col\">\r\n"
                + "  <div class=\"card shadow-lg h-100\">\r\n"
                + "    <div class=\"row g-0\">\r\n"
                + "      <div class=\"col-md-4 d-flex align-items-center justify-content-center bg-light\">\r\n"
                + "        <img src=\"logo.png\" class=\"img-fluid p-3\" alt=\"Book Cover\" style=\"max-height: 200px;\">\r\n"
                + "      </div>\r\n"
                + "      <div class=\"col-md-8\">\r\n"
                + "        <div class=\"card-body\">\r\n"
                + "          <h4 class=\"card-title text-success fw-bold\">" + bName + "</h4>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Author:</strong> <span class=\"text-primary\">" + bAuthor + "</span></p>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Order ID:</strong> <span class=\"text-secondary\">ORD" + bCode + "TM</span></p>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Status:</strong> <span class=\"badge bg-warning text-dark\">Yet to be Delivered</span></p>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Quantity Ordered:</strong> " + quantity + "</p>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Amount Paid:</strong> <span class=\"text-success\">&#8377; " + String.format("%.2f", bPrice * quantity) + "</span></p>\r\n"
                + "          <p class=\"card-text mb-1\"><strong>Quantity Available Now:</strong> " + bQty + "</p>\r\n"
                + "        </div>\r\n"
                + "      </div>\r\n"
                + "    </div>\r\n"
                + "  </div>\r\n"
                + "</div>";
    }
} 