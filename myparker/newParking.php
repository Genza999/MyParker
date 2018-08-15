<?php

$con = mysqli_connect("localhost","root","","parker");
  $link = mysqli_connect("localhost","root","","parker");

if($con)
{
    //echo "connection successfully established";


	$driverId = $_POST['driverId'];
    $driverName = $_POST['driverName'];
    $driverCar = $_POST['driverCar'];
    $parkingId = $_POST['parkingId'];

    $query_2 = "SELECT `number_of_used_slots` FROM `parking_lot` WHERE parking_lot_id = ' $parkingId'";  
    $response_2 = mysqli_query($link,$query_2); 
    $row = mysqli_fetch_assoc($response_2);
    $number_of_used_slots = $row["number_of_used_slots"];
    





    $number_of_used_slots = $number_of_used_slots + 1;

    $query_1 = "UPDATE `parking_lot` SET `number_of_used_slots` = '$number_of_used_slots' WHERE `parking_lot_id` = ' $parkingId';";
    mysqli_query($con,$query_1);






    
    $sql = "INSERT INTO `parking_record` (`parking_lot_id`, `parking_record_id`, `name`, `plate_number`, `today`, `gps_codes`) VALUES ('$parkingId', NULL, '$driverName', '$driverCar', CURRENT_TIMESTAMP, 'gps');";
    
    $query = mysqli_query($con,$sql);
    if($query){

		echo "1";

    }else{
    	echo "0";
    }
}



?>