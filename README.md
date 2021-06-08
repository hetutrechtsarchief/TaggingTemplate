# TaggingTemplate
Processing code for marking areas on a scan for tagging/annotate purposes.

Drag and Scroll to pan and zoom.

Use Shift+click to mark an area. Press 's' to save. Press 'c' to clear.

After marking the areas edit data/settings.csv to enter the names/labels/tags. Optionally add a regex in the 'remove' column.

<img src="data/screenshot.jpg" width="500">

## data/settings.csv
```csv
x,y,width,height,label,remove
2274,166,1526,106,"Naam","Naam:"
2274,322,1529,105,"Adres","Adres:"
2271,480,1062,100,"Functie","Functie:"
2268,589,1065,87,"Gewijzigd in","Gewijzigd in:"
2271,679,1066,87,"Geb.datum","Geb.datum:"
2271,763,1069,84,"Geb.plaats","Geb.plaats:"
2271,847,1066,74,"Gehuwd/ongehuwd","Gehuwd of ongehuwd:"
2274,915,1059,93,"Godsdienst","Godsdienst:"
2268,1005,1065,90,"Burgerl. beroep:","Burgerl. beroep:"
2265,1092,1065,81,"Pers. bewijs","Pers.bewijs:"
2271,1173,1062,81,"In dienst getr.","In dienst getr.:"
3340,1167,752,90,"Afgevoerd","Afgevoerd:"
49,319,2032,500,"Kinderen","Aantal kinderen:|(Beneden 18 jaar).|Voornamen:|Geb. datum"
46,822,2026,482,"Opmerkingen","Opmerkingen:"
```
