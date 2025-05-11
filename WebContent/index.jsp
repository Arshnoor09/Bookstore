<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.bittercode.model.Book" %>
<%@ page import="com.bittercode.service.BookService" %>
<%@ page import="com.bittercode.service.impl.BookServiceImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>

<%
    BookService bookService = new BookServiceImpl();
    List<Book> allBooks = bookService.getAllBooks();

    List<Book> trendingBooks = new ArrayList<>();
    List<Book> bestSellerBooks = new ArrayList<>();

    for (Book book : allBooks) {
        if (book.getQuantity() >= 20) {
            trendingBooks.add(book);
        } else if (book.getQuantity() > 0) {
            bestSellerBooks.add(book);
        }
    }

    Collections.shuffle(trendingBooks);
    Collections.shuffle(bestSellerBooks);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Store</title>
    <link rel="apple-touch-icon" sizes="180x180" href="./favicons/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="./favicons/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="./favicons/favicon-16x16.png">
    <link rel="manifest" href="./favicons/site.webmanifest">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="styles.css">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(120deg, #f9f9f9, #e0eafc);
            color: #222;
            margin: 0;
            padding: 0;
        }

        .navbar {
            padding: 0.5rem 1rem;
            background: #1b1f3b !important;
            height: 60px;
        }

        .navbar-brand img {
            height: 40px;
            width: auto;
            margin-right: 10px;
        }

        .store-name {
            color: #ffffff;
            font-size: 1.7rem;
            font-weight: 700;
        }

        .nav-link {
            color: #f0f0f0 !important;
            font-weight: 500;
            margin: 0 1rem;
            position: relative;
        }

        .nav-link:hover::after,
        .nav-link.active::after {
            content: '';
            position: absolute;
            bottom: -5px;
            left: 0;
            width: 100%;
            height: 2px;
            background-color: #f0f0f0;
        }

        .hero-section {
            padding: 6rem 0;
            text-align: center;
            background-image: url('https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?fit=crop&w=1600&q=80');
            background-size: cover;
            background-position: center;
            color: white;
            position: relative;
        }

        .hero-section::before {
            content: '';
            position: absolute;
            inset: 0;
            background-color: rgba(0, 0, 0, 0.4);
        }

        .hero-section .container {
            position: relative;
            z-index: 1;
        }

        .hero-title {
            font-size: 3.2rem;
            font-weight: 700;
            margin-bottom: 1rem;
            color: #fff;
            text-shadow: 2px 2px 5px rgba(0,0,0,0.3);
        }

        .hero-subtitle {
            font-size: 1.25rem;
            color: #eee;
            margin-bottom: 2rem;
        }

        .explore-btn {
            background-color: #ff6600;
            color: white;
            padding: 0.8rem 2rem;
            border-radius: 30px;
            font-weight: 600;
            text-decoration: none;
            border: none;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
            transition: background-color 0.3s ease;
        }

        .explore-btn:hover {
            background-color: #cc5200;
        }

        .section-container {
            padding: 4rem 0;
        }

        .section-title {
            font-size: 2.5rem;
            font-weight: 700;
            color: #333;
            text-align: center;
            position: relative;
            padding-bottom: 1rem;
        }

        .section-title:after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background-color: #ff6600;
        }

        .book-card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            background-color: #ffffff;
            overflow: hidden;
            transition: transform 0.3s ease;
        }

        .book-card:hover {
            transform: scale(1.03);
        }

        .book-card .card-body {
            padding: 1.5rem;
        }

        .book-card .card-title {
            font-weight: 700;
            color: #1b1f3b;
            font-size: 1.2rem;
        }

        .book-card .card-text {
            color: #555;
        }

        .book-card .author {
            color: #007bff;
            font-weight: 500;
        }

        .book-card .price {
            font-weight: bold;
            color: #28a745;
        }

        .book-card .trending {
            color: #e83e8c;
            font-weight: bold;
        }

        .book-card .low-stock {
            color: #dc3545;
            font-weight: bold;
        }

        .card-columns {
            column-count: 2;
        }

        @media (min-width: 768px) {
            .card-columns {
                column-count: 3;
            }
        }
    </style>
</head>
<body>

<header>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <a class="navbar-brand" href="/onlinebookstore/">
            <img src="logo.png" alt="Book Store Logo">
            <span class="store-name">Online Book Store</span>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="/onlinebookstore/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/onlinebookstore/login.html">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/onlinebookstore/CustomerRegister.html">Register</a>
                </li>
            </ul>
        </div>
    </nav>
</header>

<section class="hero-section">
    <div class="container">
        <h1 class="hero-title">Welcome to Online Book Store</h1>
        <p class="hero-subtitle">Discover a world of knowledge at your fingertips</p>
        <a href="/onlinebookstore/viewbook" class="explore-btn">Start Exploring</a>
    </div>
</section>

<section class="section-container">
    <div class="container">
        <h2 class="section-title">Trending Now</h2>
        <div class="card-columns">
            <%
                int trendingCount = 0;
                for (Book book : trendingBooks) {
                    if (trendingCount >= 3) break;
            %>
            <div class="book-card">
                <div class="card-body">
                    <h5 class="card-title"><%= book.getName() %></h5>
                    <p class="card-text">
                        Author: <span class="author"><%= book.getAuthor() %></span><br>
                        <span class="trending">Trending</span><br>
                        Price: <span class="price">₹ <%= book.getPrice() %></span>
                    </p>
                    <form action="viewbook" method="post">
                        <input type="hidden" name="selectedBookId" value="<%= book.getBarcode() %>">
                        <input type="hidden" name="qty_<%= book.getBarcode() %>" value="1">
                        <input type="submit" class="btn btn-primary" name="addToCart" value="Add To Cart">
                    </form>
                </div>
            </div>
            <%
                    trendingCount++;
                }
                if (trendingBooks.isEmpty()) {
            %>
            <div class="col-12 text-center">
                <p>No trending books available at the moment.</p>
            </div>
            <% } %>
        </div>
    </div>
</section>

<section class="section-container">
    <div class="container">
        <h2 class="section-title">Best Sellers</h2>
        <div class="card-columns">
            <%
                int bestSellerCount = 0;
                for (Book book : bestSellerBooks) {
                    if (bestSellerCount >= 3) break;
            %>
            <div class="book-card">
                <div class="card-body">
                    <h5 class="card-title"><%= book.getName() %></h5>
                    <p class="card-text">
                        Author: <span class="author"><%= book.getAuthor() %></span><br>
                        <span class="low-stock">Only <%= book.getQuantity() %> items left</span><br>
                        Price: <span class="price">₹ <%= book.getPrice() %></span>
                    </p>
                    <form action="viewbook" method="post">
                        <input type="hidden" name="selectedBookId" value="<%= book.getBarcode() %>">
                        <input type="hidden" name="qty_<%= book.getBarcode() %>" value="1">
                        <input type="submit" class="btn btn-primary" name="addToCart" value="Add To Cart">
                    </form>
                </div>
            </div>
            <%
                    bestSellerCount++;
                }
                if (bestSellerBooks.isEmpty()) {
            %>
            <div class="col-12 text-center">
                <p>No bestseller books available at the moment.</p>
            </div>
            <% } %>
        </div>
    </div>
</section>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.2.1/dist/js/bootstrap.min.js"></script>
</body>
</html>
