function get_href()     //取得所有需要监视的文件
{
    var re = []
    var p = document.getElementsByTagName('link');
    for(var i = 0;i < p.length;++i)
        re[p[i].href] = '';
    p = document.getElementsByTagName('script');
    for(var i = 0;i < p.length;++i)
        re[p[i].src] = '';
    re[window.location.href] = '';
    return re;
}

function main()
{
    var hrefs = get_href();
    setInterval(function(){
        var req = [], i=0;
        var handle = function(r, key)
            {
                return function(){
                    try{
                        if(r.readyState == 4){
                            if(r.readyState == 4){
                                if(r.status == 200){
                                    if(hrefs[key] == '')
                                        hrefs[key] = r.getResponseHeader('Last-Modified');
                                    else
                                        if(hrefs[key] != r.getResponseHeader('Last-Modified'))
                                            window.location.reload(true);
                                }
                                else
                                    console.log('cannot open ');
                            }
                        }
//                        console.log(href + r.status);
                        if(r.status == 404)
                        {
                            console.log(href + ' dose not exist');
                            hrefs[key] = 'x';
                        }
                    }
                    catch(e)
                    {
                        console.log('cannot load ' + href);
                    }
                }
            };

        for(var href in hrefs)
        {
            try{
                req[i] = new XMLHttpRequest();
                req[i].onreadystatechange = handle(req[i], href);
                req[i].open('GET', href);
                req[i].send();
                ++i;
            }
            catch(e)
            {
                console.log('error');
            }
        }
    }, 500);
}

main();