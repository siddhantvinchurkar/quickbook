var express = require('express');
var router = express.Router();
const admin = require('firebase-admin');
let serviceAccount = require('serviceAccountKey.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});
let db = admin.firestore();

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Quickbook Admin' });
});

module.exports = router;
