<?php

$con = mysqli_connect("localhost","root","","parker");
  $link = mysqli_connect("localhost","root","","parker");

if($con)
{
    //echo "connection successfully established";


	$imageInterior = $_POST['imageInterior'];
	$imageExterior = $_POST['imageExterior'];
	$name = $_POST['name'];
	$address = $_POST['address'];
	$gps_codes = $_POST['gps_codes'];
	$number_of_lots = $_POST['number_of_slots'];
	$email = $_POST['email'];
	$phoneNumber = $_POST['phoneNumber'];
	$password = $_POST['password'];


	$imageNameInteior = str_replace(" ","_",trim($phoneNumber)). "_Interor.jpg";
	$imageNameExterior = str_replace(" ","_",trim($phoneNumber)). "_Exterior.jpg";
	

    
    $sql = "INSERT INTO `parking_lot` (`parking_lot_id`, `name`, `address`, `gps_codes`, `number_of_slots`, `email`, `phone_number`, `password`, `photo_external`, `photo_internal`) VALUES (NULL, '$name', '$address', '$gps_codes', '$number_of_lots', '$email', '$phoneNumber', '$password', '$imageNameExterior', '$imageNameInteior');";
    

    $query = mysqli_query($con,$sql);
    if($query){

		$ifp = fopen("images/".$imageNameInteior, 'wb');
		fwrite($ifp, base64_decode($imageInterior));
		fclose($ifp);

		$ifp = fopen("images/".$imageNameExterior, 'wb');
		fwrite($ifp, base64_decode($imageExterior));
		fclose($ifp);

		echo "1";

    }else{
    	echo "0";
    }
}



?>