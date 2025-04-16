package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class StoreBookServlet extends HttpServlet {

    // Book service for database operations
    BookService bookService = new BookServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        // Check if the seller is logged in. If not, redirect to login.
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning text-center'>Please Login First to Continue!</div>");
            return;
        }

        // Include common header or navigation (SellerHome.html) at the top
        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);

        // Start a main container below the nav
        pw.println("<div class='container mt-4'>");

        // Check if a delete action was requested.
        String action = req.getParameter("action");
        String bookIdParam = req.getParameter("bookId");
        if ("delete".equalsIgnoreCase(action) && bookIdParam != null && !bookIdParam.trim().isEmpty()) {
            // Call deletion method from your service layer.
            String responseCode = bookService.deleteBookById(bookIdParam.trim());
            if ("SUCCESS".equalsIgnoreCase(responseCode)) {
                pw.println("<div class='alert alert-success text-center'>Book Removed Successfully</div>");
            } else {
                pw.println("<div class='alert alert-danger text-center'>Failed to remove book. Book not available.</div>");
            }
        }
        
        // Mark the active tab as storebooks (using your StoreUtil)
        StoreUtil.setActiveTab(pw, "storebooks");

        // Improved header for Book Listing – using a card header
        pw.println("<div class='card mb-4 shadow-sm'>"
                + "<div class='card-header bg-primary text-white text-center'>"
                + "<h3 class='mb-0'>Books Available in the Store</h3>"
                + "</div>"
                + "<div class='card-body'>");

        List<Book> books = bookService.getAllBooks();
        if (books == null || books.isEmpty()) {
            pw.println("<div class='alert alert-info text-center'>No Books Available in the Store</div>");
        } else {
            // Create a table for books
            pw.println("<table class='table table-hover'>");
            pw.println("<thead class='thead-dark'><tr>"
                    + "<th scope='col'>BookId</th>"
                    + "<th scope='col'>Name</th>"
                    + "<th scope='col'>Author</th>"
                    + "<th scope='col'>Price</th>"
                    + "<th scope='col'>Quantity</th>"
                    + "<th scope='col'>Action</th>"
                    + "</tr></thead><tbody>");
            for (Book book : books) {
                pw.println(getRowData(book));
            }
            pw.println("</tbody></table>");
        }
        
        // Close card-body and card container
        pw.println("</div></div>");
        
        // Close the main container
        pw.println("</div>");
    }

    /**
     * Generates a table row for a given book with Update and Delete actions side by side using a flex container.
     */
    public String getRowData(Book book) {
        return "<tr>"
                + "<th scope='row'>" + book.getBarcode() + "</th>"
                + "<td>" + book.getName() + "</td>"
                + "<td>" + book.getAuthor() + "</td>"
                // Keep the rupee symbol and number on the same line using a span
                + "<td><span>&#8377; " + book.getPrice() + "</span></td>"
                + "<td>" + book.getQuantity() + "</td>"
                + "<td>"
                    // Flex container for buttons with a gap (using Bootstrap d-flex and a custom style)
                    + "<div class='d-flex' style='gap:5px;'>"
                        // Update form pointing to the update servlet
                        + "<form method='post' action='updatebook' style='margin:0;'>"
                            + "<input type='hidden' name='bookId' value='" + book.getBarcode() + "'/>"
                            + "<button type='submit' class='btn btn-success btn-sm'>Update</button>"
                        + "</form>"
                        // Delete form handled by this same servlet
                        + "<form method='post' action='storebooks' style='margin:0;'>"
                            + "<input type='hidden' name='bookId' value='" + book.getBarcode() + "'/>"
                            + "<input type='hidden' name='action' value='delete'/>"
                            + "<button type='submit' class='btn btn-danger btn-sm' "
                            + "onclick=\"return confirm('Are you sure you want to delete this book?')\">Delete</button>"
                        + "</form>"
                    + "</div>"
                + "</td>"
                + "</tr>";
    }
}