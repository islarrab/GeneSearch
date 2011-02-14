function search (){
	x = document.getElementById("search");
	x2 = document.getElementById("parameter");
	x.innerHTML = "Showing results of: " + x2.value;
	x2.value ="";
}