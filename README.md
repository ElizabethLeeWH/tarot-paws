# tarot-paws
bootstrap template: https://github.com/StartBootstrap/startbootstrap-heroic-features.git  
api: https://github.com/ekelen/tarot-api  
tarot dataset and image reference: https://www.kaggle.com/datasets/lsind18/tarot-json/data  
cat images: Image generated with the help of ChatGPT by OpenAI  

| Method | Path | Result  | Params/ Request Body |
| ------------- | ------------- | ------------- | ------------- |
| GET  | /api/tarot/number/{num}  | get random card(s)  | params: num={integer <= 78}  |
| GET  | /api/tarot/search/{name}  | return card with specified {name}  | params: name={1 word only}  |
| GET  | /api/favourite/{user}  | return favourited cards under {user}  | params: user={"chuk", "darryl"}  |
| POST | /api/favourite  | favourite cards under {user}  | requestbody: "username":"{user}", "cardName":"{spacing allowed}" |
| POST  | /api/unfavourite  | unfavourite cards under {user}  | requestbody: "username":"{user}", "cardName":"{spacing allowed}" |
