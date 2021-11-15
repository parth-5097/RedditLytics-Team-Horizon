let final_sentiment = [];
    $("#submit_button").click(function () {
        $.ajax({
            url: "/" + document.getElementById("key").value
        }).done(function (data) {
            let d = data.split("%5097%");
            let table = document.getElementById("records_table");
            for(let i = 0;i < 10;i++){
                let row = table.insertRow(0);
                let cell1 = row.insertCell(0);
                let cell2 = row.insertCell(1);
                let cell3 = row.insertCell(2);
                let cell4 = row.insertCell(3);
                let it = JSON.parse(d[i]);
                cell1.innerHTML = new Date(it.created_utc).toLocaleDateString();
                cell2.innerHTML = `<a href="/userprofile/${it.author}">${it.author}</a>`;
                cell2.addEventListener("click",function(){
                    window.location.href = "/userprofile/" + it.author;
                });
                cell3.innerHTML = `<a href="/subreddit/${it.subreddit}">${it.subreddit}</a>`;
                cell3.addEventListener("click",function(){
                    window.location.href = "/subreddit/" + it.subreddit;
                });
                cell4.innerHTML = it.title;
            }
            let row2 = table.insertRow(0);
            row2.insertCell(0);
            row2.insertCell(1);
            row2.insertCell(2);
            let cell5 = row2.insertCell(3);
            cell5.style = "font-size:20px";
            cell5.innerHTML = new String("Search Term : " + document.getElementById("key").value).bold()+"   (Sentiment Analyser is running....)";

//            for(let i = 0;i < d.length;i++){
//                getNLPData(JSON.parse(d[i]).selftext,cell5);
//            }
        });
    });

    $("#btn").click(function () {
        window.location.href = "/search/" + document.getElementById("key").value;
    });

    function getNLPData(text,cell5){
        if(text != ""){
            $.ajax({
                "url": "/sentiment",
                "method": "POST",
                "timeout": 0,
                "headers": {
                    "Content-Type": "application/json"
                },
                "data": JSON.stringify({
                    text
                }),
            }).done(function(data){
                final_sentiment.push(parseInt(data));
                const map = final_sentiment.reduce((acc, e) => acc.set(e, (acc.get(e) || 0) + 1), new Map());
                cell5.innerHTML = new String("Search Term : " + document.getElementById("key").value).bold()+"("+[...map.entries()].reduce((a, e) => e[1] > a[1] ? e : a)+")" +"        Sentiment Results : " + [...map.entries()];
            });
        }
    }