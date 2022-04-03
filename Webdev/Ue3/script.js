var arr_rezept = [document.getElementById("Salami"),document.getElementById("Schinken"),
document.getElementById("Ananas"), document.getElementById("Pilze")];

var pizza_price = {mini:5, maxi:8, party:10};
var pizza_calorie = {mini:300, maxi:500, party:800};
var pizza_size = {mini:15, maxi:30, party:45};

var pizza;
var price = 0;
var calories = 0;
var zutaten = "";

function order() {

    if(document.getElementById("pizza").value == "Mini (15cm)") {
        pizza = "mini";
    }
    else if (document.getElementById("pizza").value == "Maxi (30cm)") {
        pizza = "maxi";
    } else {
        pizza = "party";
    }
    zutaten = "";

    for (x of arr_rezept) {
        if(x.checked) {
            price += pizza_price[pizza];
            calories+= pizza_calorie[pizza];
            zutaten += x.value + " ";
        }
    }
    if (!arr_rezept[0].checked && !arr_rezept[1].checked && !arr_rezept[2].checked && !arr_rezept[3].checked) {
            alert("Zutat auswaehlen");
            //return;

    }
    else {
        var tr = document.createElement('tr');   //erstellt neue Zeile
        var td_size = document.createElement('td'); //erstellt neue Spalten nach je nach reihenfogle
        var td_receipe = document.createElement('td');
        var td_price = document.createElement('td');
        var td_calories = document.createElement('td');

        td_size.innerHTML = pizza_size[pizza];
        td_receipe.innerHTML = zutaten;
        td_price.innerHTML = pizza_price[pizza];
        td_calories.innerHTML = pizza_calorie[pizza];

        //fuegt der Zeile Spalten hinzu
        tr.appendChild(td_size); 
        tr.appendChild(td_receipe);
        tr.appendChild(td_price);
        tr.appendChild(td_calories);

        //fuegt zeile hinzu

        document.getElementById("Zutaten").appendChild(tr);

        console.log(document.getElementById("price").innerHTML);

        document.getElementById("price").innerHTML = parseInt(document.getElementById("price").innerHTML) + pizza_price[pizza];
    }

    
}



document.getElementById("bestellen").onclick = order;
