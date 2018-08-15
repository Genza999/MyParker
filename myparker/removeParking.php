<?php

$con = mysqli_connect("localhost","root","","parker");
  $link = mysqli_connect("localhost","root","","parker");

if($con)
{
    //echo "connection successfully established";


	$driverName = $_POST['driverName'];


  $query_2 = "SELECT parking_lot_id FROM `parking_record` WHERE name = '$driverName'";
  $response_2 = mysqli_query($link,$query_2); 
  $row = mysqli_fetch_assoc($response_2);
  $parkingId = $row["parking_lot_id"];



  $query_1 = "SELECT `number_of_used_slots` FROM `parking_lot` WHERE parking_lot_id = ' $parkingId'";  
  $response_1 = mysqli_query($link,$query_1); 
  $row = mysqli_fetch_assoc($response_1);
  $number_of_used_slots = $row["number_of_used_slots"];
  


      $number_of_used_slots = $number_of_used_slots - 1;
      $query_0 = "UPDATE `parking_lot` SET `number_of_used_slots` = ' $number_of_used_slots' WHERE `parking_lot`.`parking_lot_id` = ' $parkingId';";
      mysqli_query($con,$query_0);
  
  
  
  







    
    $sql = "DELETE FROM `parking_record` WHERE `name` = '$driverName';";
    
    $query = mysqli_query($con,$sql);
    if($query){

		echo "1";

    }else{
    	echo "0";
    }
}



?>