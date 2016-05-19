<?php
#echo 'what????'		
		$myName = $_GET['myName'];
		$comment = $_GET['comment'];
		$SongName = $_GET['SongName'];
		$UserID =$_GET['UserID'];
	
		if($comment==''){
			echo 'please fill all values';
		}else{
			

		$con = mysqli_connect(HOST,USER,PASS,DB) or die("접속불가");

			#require_once('dbConnect.php');
			#$sql = "SELECT * FROM users WHERE username='$username' ";
			
			#$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			#if(isset($check)){
			#	echo 'username already exist';
			#}else{				
		$sql = "INSERT INTO comments (UserID,SongName,myName,comment) VALUES('$UserID','$SongName','$myName','$comment')";
		if(mysqli_query($con,$sql)){
			echo 'Successed comment!';
		mysqli_close($con);
		$con = mysqli_connect(HOST,USER,PASS,DB) or die("접속불가");		$sql2 ="UPDATE songDB set commentCount = (commentCount +1) where SongName ='$SongName' AND UserID ='$UserID'";
			if(mysqli_query($con2,$sql2)) {echo 'succesed count up!';
				mysqli_close($con2);
			}
			else echo ' 실패 ';
		}else{
			echo 'oops! Please try again!';
		}
		}
?>		
