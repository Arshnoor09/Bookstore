package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class ViewBookServlet extends HttpServlet {

    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        if (!StoreUtil.isLoggedIn(UserRole.CUSTOMER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning text-center'>Please Login First to Continue!</div>");
            return;
        }
        try {
            List<Book> books = bookService.getAllBooks();

            RequestDispatcher rd = req.getRequestDispatcher("CustomerHome.html");
            rd.include(req, res);

            StoreUtil.setActiveTab(pw, "books");

            // New Styled Available Books Header
            pw.println("<div class='container mb-4'>"
                    + "  <div class='row align-items-center bg-primary text-white p-3 rounded'>"
                    + "    <div class='col-md-8'>"
                    + "      <h3 class='mb-0'>Available Books</h3>"
                    + "    </div>"
                    + "    <div class='col-md-4 text-md-right mt-3 mt-md-0'>"
                    + "      <form action='cart' method='post'>"
                    + "        <button type='submit' class='btn btn-light font-weight-bold'>Proceed to Cart</button>"
                    + "      </form>"
                    + "    </div>"
                    + "  </div>"
                    + "</div>");

            pw.println("<div class='container'>"
                    + "<div class='row'>");

            StoreUtil.updateCartItems(req);

            HttpSession session = req.getSession();
            for (Book book : books) {
                pw.println(this.addBookToCard(session, book));
            }

            pw.println("</div>"); // close row

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addBookToCard(HttpSession session, Book book) {
        String bCode = book.getBarcode();
        int bQty = book.getQuantity();

        int cartItemQty = 0;
        if (session.getAttribute("qty_" + bCode) != null) {
            cartItemQty = (int) session.getAttribute("qty_" + bCode);
        }

        String button = "";
        if (bQty > 0) {
            button = (cartItemQty == 0
                    ? "<form action='' method='post'>" // action changed to empty string
                        + "<input type='hidden' name='selectedBookId' value='" + bCode + "'/>"
                        + "<input type='hidden' name='qty_" + bCode + "' value='1'/>"
                        + "<button type='submit' name='addToCart' class='btn btn-primary btn-block'>Add To Cart</button>"
                        + "</form>"
                    : "<form method='post' action='' class='d-flex justify-content-between align-items-center'>"
                        + "<input type='hidden' name='selectedBookId' value='" + bCode + "'/>"
                        + "<button type='submit' name='removeFromCart' class='btn btn-danger btn-sm'>-</button>"
                        + "<span class='px-2'>" + cartItemQty + "</span>"
                        + "<button type='submit' name='addToCart' class='btn btn-success btn-sm'>+</button>"
                        + "</form>");
        } else {
            button = "<p class='btn btn-danger btn-block'>Out Of Stock</p>";
        }

        return "<div class='col-md-4 mb-4'>"
                + "<div class='card h-100 shadow-sm'>"
                + "  <img class='card-img-top' src='logo.png' alt='Book Image' style='height:200px; object-fit:contain;'>"
                + "  <div class='card-body d-flex flex-column'>"
                + "    <h5 class='card-title text-success'>" + book.getName() + "</h5>"
                + "    <p class='card-text'>"
                + "      <strong>Author:</strong> <span class='text-primary'>" + book.getAuthor() + "</span><br>"
                + "      <strong>Id:</strong> " + bCode + "<br>"
                + (bQty < 20 ? "<span class='text-danger'>Only " + bQty + " left</span>"
                             : "<span class='text-success'>Trending</span>") + "<br>"
                + "      <strong>Price:</strong> <span style='color:green;'>&#8377; " + book.getPrice() + "</span>"
                + "    </p>"
                + "    <div class='mt-auto'>" + button + "</div>"
                + "  </div>"
                + "</div>"
                + "</div>";
    }
}