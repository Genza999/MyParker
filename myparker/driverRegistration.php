<?php

$con = mysqli_connect("localhost","root","","parker");
  $link = mysqli_connect("localhost","root","","parker");

if($con)
{
    //echo "connection successfully established";


	$name = $_POST['name'];
	$carNumberPlate = $_POST['carNumberPlate'];
	$phoneNumber = $_POST['phoneNumber'];
	$email = $_POST['email'];
	$password = $_POST['password'];

    
    $sql = "INSERT INTO `driver` (`driver_id`, `name`, `phone_number`, `car_number_plate`, `email`, `password`) VALUES (NULL, '$name', '$phoneNumber', '$carNumberPlate','$email', '$password');";
    
    $query = mysqli_query($con,$sql);
    if($query){

		echo "1";

    }else{
    	echo "0";
    }
}



?>