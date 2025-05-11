package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bittercode.constant.BookStoreConstants;
import com.bittercode.constant.db.BooksDBConstants;
import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class AddBookServlet extends HttpServlet {
    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        String bName = req.getParameter(BooksDBConstants.COLUMN_NAME);
        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "addbook");
        pw.println("<div class='container my-2'>");

        if (bName == null || bName.trim().isEmpty()) {
            // Render the add book form without message
            showAddBookForm(pw, "", "", "", "", "", false);  // Pass false since no message is shown
            return;
        }

        try {
            String bCode = req.getParameter("bookCode");
            if (bCode == null || bCode.trim().isEmpty()) {
                bCode = java.util.UUID.randomUUID().toString();
            }

            String bAuthor = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
            double bPrice = Double.parseDouble(req.getParameter(BooksDBConstants.COLUMN_PRICE));
            int bQty = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_QUANTITY));

            Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
            String message = bookService.addBook(book);

            if ("SUCCESS".equalsIgnoreCase(message)) {
                // Show success message
                showAddBookForm(pw, "", "", "", "", "Book Added Successfully!", true);  // Pass true for success message
            } else {
                showAddBookForm(pw, bCode, bName, bAuthor, String.valueOf(bPrice), "Failed to Add Book! Please try again.", false);  // Pass false for failure message
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAddBookForm(pw, "", "", "", "", "Failed to Add Book! Fill up Carefully.", false);  // Pass false for failure message
        }
    }

    private static void showAddBookForm(PrintWriter pw, String bookCode, String bookName, String bookAuthor, String bookPrice, String message, boolean isSuccess) {
        String dismissScript = "<script>"
                + "setTimeout(function(){ document.getElementById('successMessage').style.display = 'none'; }, 5000);"
                + "function dismissMessage() { document.getElementById('successMessage').style.display = 'none'; }"
                + "</script>";

        String messageHtml = "";
        if (message != null && !message.trim().isEmpty()) {
            String alertClass = isSuccess ? "alert-success" : "alert-danger";
            messageHtml = "<div id=\"successMessage\" class=\"alert " + alertClass + " alert-dismissible\" role=\"alert\">"
                    + message
                    + "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" onclick=\"dismissMessage()\">"
                    + "<span aria-hidden=\"true\">&times;</span></button></div>";
        }

        String form = "<style>"
                + ".form-label { display: block; margin-bottom: 8px; font-weight: bold; }"
                + ".form-input { width: 100%; padding: 10px; margin-bottom: 15px; border-radius: 4px; border: 1px solid #ccc; }"
                + ".form-container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; border-radius: 8px; }"
                + "</style>"
                + dismissScript
                + "<div class='form-container'>"
                + messageHtml
                + "<form action=\"addbook\" method=\"post\">"
                + "<div><label for=\"bookCode\" class=\"form-label\">Book Code:</label><input type=\"text\" name=\"bookCode\" id=\"bookCode\" class=\"form-input\" placeholder=\"Enter Book Code\" value=\"" + bookCode + "\"></div>"
                + "<div><label for=\"bookName\" class=\"form-label\">Book Name:</label><input type=\"text\" name=\"name\" id=\"bookName\" class=\"form-input\" placeholder=\"Enter Book's name\" value=\"" + bookName + "\" required></div>"
                + "<div><label for=\"bookAuthor\" class=\"form-label\">Book Author:</label><input type=\"text\" name=\"author\" id=\"bookAuthor\" class=\"form-input\" placeholder=\"Enter Author's Name\" value=\"" + bookAuthor + "\" required></div>"
                + "<div><label for=\"bookPrice\" class=\"form-label\">Book Price:</label><input type=\"number\" name=\"price\" class=\"form-input\" placeholder=\"Enter the Price\" value=\"" + bookPrice + "\" required></div>"
                + "<div><label for=\"bookQuantity\" class=\"form-label\">Book Quantity:</label><input type=\"number\" name=\"quantity\" id=\"bookQuantity\" class=\"form-input\" placeholder=\"Enter the quantity\" value=\"" + bookPrice + "\" required></div>"
                + "<div><input class=\"btn btn-success my-2\" type=\"submit\" value=\"Add Book\"></div>"
                + "</form>"
                + "</div>";

        pw.println(form);
    }
}