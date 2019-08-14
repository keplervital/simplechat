const express = require('express');
const app = express();
const port = (process.env.PORT || 3000);

app.get('/', function(req, res){
	res.send('Simple chat!');
});

app.listen(port, () => console.log(`Simplechat server listening on port ${port}.`));