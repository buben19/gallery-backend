export default function() {

  this.namespace = '/api';

  this.get('/images', () => {
    return {
             "_embedded" : {
               "images" : [ {
                 "title" : "title 1",
                 "path" : "path 1",
                 "_links" : {
                   "self" : {
                     "href" : "http://localhost/api/images/1"
                   },
                   "image" : {
                     "href" : "http://localhost/api/images/1"
                   }
                 }
               }, {
                 "title" : "title 2",
                 "path" : "path 2",
                 "_links" : {
                   "self" : {
                     "href" : "http://localhost/api/images/2"
                   },
                   "image" : {
                     "href" : "http://localhost/api/images/2"
                   }
                 }
               } ]
             },
             "_links" : {
               "self" : {
                 "href" : "http://localhost/api/images"
               },
               "profile" : {
                 "href" : "http://localhost/api/profile/images"
               }
             }
           };
  });
}
