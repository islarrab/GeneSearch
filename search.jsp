<html>

   <head>
      <link rel="stylesheet" href="stylesheet.css" />
      <style type="text/css">
      </style>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
      <script language="Javascript" type="text/javascript" src="javascript.js">
      </script>
      <title>Search</title>
   </head>

   <body>
      <FORM action="Search">
	     <fieldset class="search">
			<div class="searchbar">
				<input type="text" name="search" size="50" />
				<select name="type" class="searchoptions">
					<option>Gene</option>
				</select>
			</div>
			<div class="searchbutton">
				<input type="submit" id="boton_buscar" value="Search" onClick="search()"/>
			</div>
		 </fieldset>
      </FORM>

      <fieldset class="results"><legend>Results</legend>
         <p id="search">Search Results<p>
      </fieldset>
   </body>

</html>

