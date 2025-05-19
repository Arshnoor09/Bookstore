package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;

import com.bittercode.model.Book;
import com.bittercode.model.UserRole;
import com.bittercode.service.BookService;
import com.bittercode.service.impl.BookServiceImpl;
import com.bittercode.util.StoreUtil;

public class StoreBookServlet extends HttpServlet {

    BookService bookService = new BookServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        boolean isAjax = "true".equalsIgnoreCase(req.getParameter("ajax"));

        List<Book> books = bookService.getAllBooks();

        // Filter and sort
        String search = req.getParameter("search");
        String sortBy = req.getParameter("sort");

        if (search != null && !search.trim().isEmpty()) {
            String keyword = search.trim().toLowerCase();
            books = books.stream()
                    .filter(b -> b.getBarcode().toLowerCase().contains(keyword) ||
                                 b.getName().toLowerCase().contains(keyword) ||
                                 b.getAuthor().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparingDouble(Book::getPrice));
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparingDouble(Book::getPrice).reversed());
        } else if ("quantity_asc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparingInt(Book::getQuantity));
        } else if ("quantity_desc".equalsIgnoreCase(sortBy)) {
            books.sort(Comparator.comparingInt(Book::getQuantity).reversed());
        }

        if (isAjax) {
            // Only return updated table body
            if (books == null || books.isEmpty()) {
                pw.println("<tr><td colspan='6' class='text-center'>No books found</td></tr>");
            } else {
                for (Book book : books) {
                    pw.println(getRowData(book));
                }
            }
            return;
        }

        // Normal page rendering
        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning text-center'>Please Login First to Continue!</div>");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);

        pw.println("<div class='container mt-4'>");

        String action = req.getParameter("action");
        String bookIdParam = req.getParameter("bookId");
        if ("delete".equalsIgnoreCase(action) && bookIdParam != null && !bookIdParam.trim().isEmpty()) {
            String responseCode = bookService.deleteBookById(bookIdParam.trim());
            if ("SUCCESS".equalsIgnoreCase(responseCode)) {
                pw.println("<div class='alert alert-success text-center'>Book Removed Successfully</div>");
            } else {
                pw.println("<div class='alert alert-danger text-center'>Failed to remove book. Book not available.</div>");
            }
        }

        StoreUtil.setActiveTab(pw, "storebooks");

        // Search and sort form
        pw.println("<div class='card mb-4 shadow-sm'>"
                + "<div class='card-header bg-primary text-white text-center'>"
                + "<h3 class='mb-0'>Books Available in the Store</h3>"
                + "</div><div class='card-body'>");

        pw.println("<div class='row mb-3'>"
                + "<div class='col-md-6'>"
                + "<input type='text' class='form-control' id='searchBox' placeholder='Search by Book ID, Name, Author' />"
                + "</div>"
                + "<div class='col-md-3'>"
                + "<select class='form-control' id='sortBy'>"
                + "<option value=''>Sort By</option>"
                + "<option value='price_asc'>Price: Low to High</option>"
                + "<option value='price_desc'>Price: High to Low</option>"
                + "<option value='quantity_asc'>Quantity: Low to High</option>"
                + "<option value='quantity_desc'>Quantity: High to Low</option>"
                + "</select>"
                + "</div>"
                + "</div>");

        pw.println("<table class='table table-hover'>");
        pw.println("<thead class='thead-dark'><tr>"
                + "<th>Book ID</th><th>Name</th><th>Author</th><th>Price</th><th>Quantity</th><th>Action</th>"
                + "</tr></thead><tbody id='bookTableBody'>");

        if (books == null || books.isEmpty()) {
            pw.println("<tr><td colspan='6' class='text-center'>No books available</td></tr>");
        } else {
            for (Book book : books) {
                pw.println(getRowData(book));
            }
        }

        pw.println("</tbody></table></div></div></div>");

        // JavaScript for AJAX filtering/sorting
        pw.println("<script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>");
        pw.println("<script>"
                + "function loadBooks() {"
                + "  const search = $('#searchBox').val();"
                + "  const sort = $('#sortBy').val();"
                + "  $.post('storebooks?ajax=true', { search, sort }, function(data) {"
                + "    $('#bookTableBody').html(data);"
                + "  });"
                + "}"
                + "$('#searchBox').on('input', loadBooks);"
                + "$('#sortBy').on('change', loadBooks);"
                + "</script>");
    }

    public String getRowData(Book book) {
        return "<tr>"
                + "<th>" + book.getBarcode() + "</th>"
                + "<td>" + book.getName() + "</td>"
                + "<td>" + book.getAuthor() + "</td>"
                + "<td>&#8377; " + book.getPrice() + "</td>"
                + "<td>" + book.getQuantity() + "</td>"
                + "<td><div class='d-flex' style='gap:5px;'>"
                + "<form method='post' action='updatebook' style='margin:0;'>"
                + "<input type='hidden' name='bookId' value='" + book.getBarcode() + "'/>"
                + "<button class='btn btn-success btn-sm'>Update</button>"
                + "</form>"
                + "<form method='post' action='storebooks' style='margin:0;'>"
                + "<input type='hidden' name='bookId' value='" + book.getBarcode() + "'/>"
                + "<input type='hidden' name='action' value='delete'/>"
                + "<button class='btn btn-danger btn-sm' onclick=\"return confirm('Delete this book?')\">Delete</button>"
                + "</form></div></td></tr>";
    }
}