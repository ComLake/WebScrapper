<html>
<head>
<title>"Ulake Crawler"</title>
<style>
    body{
        margin: 0;
        padding: 0;
        font-family: sans-serif;
        background: #ff9800;
    }
    .box{
        width: 300px;
        padding: 40px;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        background: #263238;
        text-align: center;
    }
    .box input[type="text"],.box input[type="password"]{
        border:0;
        background:none;
        display:block;
        margin: 20px auto;
        text-align:center;
        border: 2px solid #ffb74d;
        padding: 14px 10px;
        width: 200px;
        outline: none;
        color: white;
        border-radius: 24px;
        transition: 0.25s;
    }
    .box h1{
        color: white;
        text-transform:uppercase;
        font-weight: 500;
    }
    .box input[type="text"]:focus,.box input[type="password"]:focus{
        width: 280px;
        border-color: #ef5350;
    }
    .box input[type="submit"]{
        border:0;
        background:none;
        display:block;
        margin: 20px auto;
        text-align:center;
        border: 2px solid #42a5f5;
        padding: 14px 40px;
        outline: none;
        color: white;
        border-radius: 24px;
        transition: 0.25s;
    }
    .box input[type="submit"]:hover{
        background: #42a5f5;
    }
</style>
</head>
<body>
<form class="box" action=action="<%= request.getContextPath() %>" method="post">
    <h1>ULake Login</h1>
    <input type="text" name="" placeholder="Username">
    <input type="text" name="" placeholder="Password">
    <input type="submit" name="" value="Login">
</form>
</body>
</html>
