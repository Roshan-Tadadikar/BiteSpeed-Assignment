**Requirements**

You are required to design a web service with an endpoint /identify that will receive HTTP POST requests with
JSON body of the following format:
{
"email"?: string,
"phoneNumber"?: number
}
The web service should return an HTTP 200 response with a JSON payload containing the consolidated contact.
Your response should be in this format:

Bitespeed Backend Task: Identity Reconciliation 3

{
"contact":{
"primaryContatctId": number,
"emails": string[], // first element being email of primary contact
"phoneNumbers": string[], // first element being phoneNumber of primary conta
"secondaryContactIds": number[] // Array of all Contact IDs that are "seconda
}
}

Extending the previous example:

Request:
{
"email": "mcfly@hillvalley.edu",
"phoneNumber": "123456"
}
will give the following response

{
"contact":{
"primaryContatctId": 1,
"emails": ["lorraine@hillvalley.edu","mcfly@hillvalley.edu"]
"phoneNumbers": ["123456"]
"secondaryContactIds": [23]
}
}

In fact, all of the following requests will return the above response (use toggle to expand)

{
"email": null,
"phoneNumber":"123456"
}

{
"email": "lorraine@hillvalley.edu",
"phoneNumber": null
}

{
"email": "mcfly@hillvalley.edu",
"phoneNumber": null
}

But what happens if there are no existing contacts against an incoming request?
The service will simply create a new Contact row with linkPrecedence=”primary" treating it as a new customer and
return it with an empty array for secondaryContactIds

Bitespeed Backend Task: Identity Reconciliation 4
When is a secondary contact created?
If an incoming request has either of phoneNumber or email common to an existing contact but contains new
information, the service will create a “secondary” Contact row.
Example:
Existing state of database:
{
id 1
phoneNumber "123456"
email "lorraine@hillvalley.edu"
linkedId null
linkPrecedence "primary"
createdAt 2023-04-01 00:00:00.374+00
updatedAt 2023-04-01 00:00:00.374+00
deletedAt null
}
Identify request:
{
"email":"mcfly@hillvalley.edu",
"phoneNumber":"123456"
}
New state of database:
{
id 1
phoneNumber "123456"
email "lorraine@hillvalley.edu"
linkedId null
linkPrecedence "primary"
createdAt 2023-04-01 00:00:00.374+00
updatedAt 2023-04-01 00:00:00.374+00
deletedAt null
},
{
id 23
phoneNumber "123456"
email "mcfly@hillvalley.edu"
linkedId 1
linkPrecedence "secondary"
createdAt 2023-04-20 05:30:00.11+00
updatedAt 2023-04-20 05:30:00.11+00
deletedAt null
},

Can primary contacts turn into secondary?
Yes. Let’s take an example
Existing state of database:
{
id 11
phoneNumber "919191"

Bitespeed Backend Task: Identity Reconciliation 5

email "george@hillvalley.edu"
linkedId null
linkPrecedence "primary"
createdAt 2023-04-11 00:00:00.374+00
updatedAt 2023-04-11 00:00:00.374+00
deletedAt null
},
{
id 27
phoneNumber "717171"
email "biffsucks@hillvalley.edu"
linkedId null
linkPrecedence "primary"
createdAt 2023-04-21 05:30:00.11+00
updatedAt 2023-04-21 05:30:00.11+00
deletedAt null
}
Request:
{
"email":"george@hillvalley.edu",
"phoneNumber": "717171"
}
New state of database:
{
id 11
phoneNumber "919191"
email "george@hillvalley.edu"
linkedId null
linkPrecedence "primary"
createdAt 2023-04-11 00:00:00.374+00
updatedAt 2023-04-11 00:00:00.374+00
deletedAt null
},
{
id 27
phoneNumber "717171"
email "biffsucks@hillvalley.edu"
linkedId 11
linkPrecedence "secondary"
createdAt 2023-04-21 05:30:00.11+00
updatedAt 2023-04-28 06:40:00.23+00
deletedAt null
}
Response:
{
"contact":{
"primaryContatctId": 11,
"emails": ["george@hillvalley.edu","biffsucks@hillvalley.edu"]
"phoneNumbers": ["919191","717171"]
"secondaryContactIds": [27]
}


**Link** : https://bitespeed-assignment-production.up.railway.app/identify
}
