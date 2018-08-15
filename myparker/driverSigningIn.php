<?php  

	$database_name = "parker";
	$host = "localhost";
	$user_name = "root";
	$password = "";

  $link = mysqli_connect($host,$user_name,$password,$database_name);

 	 $email = $_POST['email'];
	 $password = $_POST['password'];


$qry1 ="SELECT * FROM driver where  email = '$email' AND password='$password' ";

				$database_array = array();
				
				$response = mysqli_query($link,$qry1);  									;
												
							
                $database_array = array();

                while ( $row = mysqli_fetch_assoc($response) ) {
                  array_push($database_array, $row);
                }   

				echo json_encode($database_array);	

?>