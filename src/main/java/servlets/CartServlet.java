package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.constant.BookStoreConstants;
import com.bittercode.model.Book;
import com.bittercode.model.Cart;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class CartServlet extends HttpServlet {

    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<div style='padding: 20px; font-weight:bold; color: red; text-align:center;'>Please Login First to Continue!!</div>");
            return;
        }

        try {
            StoreUtil.updateCartItems(req);
            HttpSession session = req.getSession();
            String bookIds = (session.getAttribute("items") != null) ? (String) session.getAttribute("items") : "";

            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            StoreUtil.setActiveTab(pw, "cart");

            List<Book> books = bookService.getBooksByCommaSeperatedBookIds(bookIds);
            List<Cart> cartItems = new ArrayList<>();

            // Full Page Background Styling
            pw.println("<style>");
            pw.println("body { background-color: #f4f7fc; font-family: Arial, sans-serif; margin: 0; padding: 0; }");
            pw.println(".cart-container { background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }");
            pw.println(".cart-table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            pw.println(".cart-table th, .cart-table td { padding: 10px; border-bottom: 1px solid #ddd; text-align: left; }");
            pw.println(".cart-table th { background-color: #f5f5f5; color: #333; }");
            pw.println(".cart-table tr:nth-child(even) { background-color: #f9f9f9; }");
            pw.println(".cart-table .total-row { background-color: #fafafa; font-weight: bold; }");
            pw.println("</style>");

            // Main Cart Container
            pw.println("<div class='cart-container'>");
            pw.println("<h2 style='text-align:center; font-weight:600; color:#333;'>Shopping Cart</h2>");
            pw.println("<table class='cart-table'>");
            pw.println("<thead>");
            pw.println("<tr>");
            pw.println("<th>Book ID</th>");
            pw.println("<th>Name</th>");
            pw.println("<th>Author</th>");
            pw.println("<th>Price/Item</th>");
            pw.println("<th>Quantity</th>");
            pw.println("<th>Amount</th>");
            pw.println("</tr>");
            pw.println("</thead>");
            pw.println("<tbody>");

            double amountToPay = 0;

            if (books == null || books.isEmpty()) {
                pw.println("<tr><td colspan='6' style='text-align:center; padding:15px; color:#999;'>No items in cart.</td></tr>");
            } else {
                for (Book book : books) {
                    int qty = (int) session.getAttribute("qty_" + book.getBarcode());
                    Cart cart = new Cart(book, qty);
                    cartItems.add(cart);
                    amountToPay += (qty * book.getPrice());
                    pw.println(getRowData(cart));
                }
            }

            session.setAttribute("cartItems", cartItems);
            session.setAttribute("amountToPay", amountToPay);

            // Total Section
            if (amountToPay > 0) {
                pw.println("<tr class='total-row'>");
                pw.println("<td colspan='5' style='text-align:right;'>Total:</td>");
                pw.println("<td>&#8377; " + amountToPay + "</td>");
                pw.println("</tr>");
            }

            pw.println("</tbody>");
            pw.println("</table>");

            // Proceed to Payment Button
            if (amountToPay > 0) {
                pw.println("<div style='text-align:right; margin-top:20px;'>");
                pw.println("<form action='checkout' method='post'>");
                pw.println("<button type='submit' name='pay' style='background-color:#007bff; color:white; border:none; padding:10px 20px; border-radius:4px; font-size:16px; cursor:pointer;'>Proceed to Pay &#8377; " + amountToPay + "</button>");
                pw.println("</form>");
                pw.println("</div>");
            }

            pw.println("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRowData(Cart cart) {
        Book book = cart.getBook();
        return "<tr>"
                + "<td>" + book.getBarcode() + "</td>"
                + "<td>" + book.getName() + "</td>"
                + "<td>" + book.getAuthor() + "</td>"
                + "<td>&#8377; " + book.getPrice() + "</td>"
                + "<td>"
                + "<form method='post' action='cart' style='display:inline;'>"
                + "<input type='hidden' name='selectedBookId' value='" + book.getBarcode() + "'/>"
                + "<button type='submit' name='removeFromCart' style='padding:5px 10px; background-color:#f44336; color:white; border:none; border-radius:3px;'>-</button>"
                + "<span style='margin: 0 10px;'>" + cart.getQuantity() + "</span>"
                + "<button type='submit' name='addToCart' style='padding:5px 10px; background-color:#4caf50; color:white; border:none; border-radius:3px;'>+</button>"
                + "</form>"
                + "</td>"
                + "<td>&#8377; " + (book.getPrice() * cart.getQuantity()) + "</td>"
                + "</tr>";
    }
}
