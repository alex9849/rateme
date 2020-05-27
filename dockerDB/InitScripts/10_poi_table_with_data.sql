USE osm01;

DROP TABLE IF EXISTS rateme_poi;

CREATE TABLE rateme_poi (
  `osm_id` bigint PRIMARY KEY
  , position GEOMETRY not null   	
  ,`poi_type` varchar(255) not null 
  , SPATIAL INDEX(position)
) CHARACTER SET utf8mb4;


INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(1147280365,st_GeomFromText("POINT (7.3928144 49.2564739)"),'restaurant')
,(1152859123,st_GeomFromText("POINT (7.378253 49.2559929)"),'restaurant')
,(1276858853,st_GeomFromText("POINT (7.3762128 49.2512206)"),'restaurant')
,(1279060180,st_GeomFromText("POINT (7.3563125 49.2352176)"),'restaurant')
,(1363057445,st_GeomFromText("POINT (7.4014643 49.2297023)"),'restaurant')
,(1469792575,st_GeomFromText("POINT (7.337094 49.3158463)"),'restaurant')
,(1473293724,st_GeomFromText("POINT (7.3799948 49.2407401)"),'restaurant')
,(1782038707,st_GeomFromText("POINT (7.343502 49.3089341)"),'cafe')
,(2156402224,st_GeomFromText("POINT (7.3647401 49.2453926)"),'fast_food')
,(2156402225,st_GeomFromText("POINT (7.3651317 49.2456621)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(2209576537,st_GeomFromText("POINT (7.3650158 49.2467856)"),'restaurant')
,(2209576572,st_GeomFromText("POINT (7.3653109 49.2471638)"),'cafe')
,(2241800925,st_GeomFromText("POINT (7.280491 49.3110983)"),'swingerclub')
,(2252401658,st_GeomFromText("POINT (7.3057744 49.2992949)"),'restaurant')
,(2255854800,st_GeomFromText("POINT (7.3644718 49.2298542)"),'restaurant')
,(2282338093,st_GeomFromText("POINT (7.3885985 49.2561454)"),'restaurant')
,(2407812797,st_GeomFromText("POINT (7.3847179 49.2547671)"),'pub')
,(2418775692,st_GeomFromText("POINT (7.3663957 49.2481207)"),'pub')
,(2711355137,st_GeomFromText("POINT (7.2788349 49.3171646)"),'restaurant')
,(2711355142,st_GeomFromText("POINT (7.2780478 49.3171778)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(279012707,st_GeomFromText("POINT (7.3521473 49.2487695)"),'fast_food')
,(279271165,st_GeomFromText("POINT (7.3547024 49.2473883)"),'fast_food')
,(2825293460,st_GeomFromText("POINT (7.3392678 49.3210782)"),'pub')
,(2855126459,st_GeomFromText("POINT (7.3745463 49.3126506)"),'restaurant')
,(29444682,st_GeomFromText("POINT (7.295332 49.3079751)"),'restaurant')
,(302253799,st_GeomFromText("POINT (7.2781062 49.3097)"),'restaurant')
,(302253802,st_GeomFromText("POINT (7.2833418 49.3084452)"),'pub')
,(302253804,st_GeomFromText("POINT (7.2938729 49.3082437)"),'restaurant')
,(3072521833,st_GeomFromText("POINT (7.364833 49.246623)"),'bar')
,(3110038842,st_GeomFromText("POINT (7.3655404 49.254022)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(3188600462,st_GeomFromText("POINT (7.3498268 49.2527132)"),'restaurant')
,(3218560674,st_GeomFromText("POINT (7.32029 49.2925)"),'pub')
,(3220191605,st_GeomFromText("POINT (7.3488559 49.3163181)"),'cafe')
,(330339540,st_GeomFromText("POINT (7.3619222 49.2493301)"),'restaurant')
,(332050722,st_GeomFromText("POINT (7.3646986 49.2479741)"),'fast_food')
,(332251156,st_GeomFromText("POINT (7.3446136 49.2537675)"),'pub')
,(332253259,st_GeomFromText("POINT (7.3623113 49.2524846)"),'pub')
,(334320901,st_GeomFromText("POINT (7.3616342 49.2501388)"),'pub')
,(334329215,st_GeomFromText("POINT (7.361081 49.2466842)"),'fast_food')
,(3347609094,st_GeomFromText("POINT (7.3436565 49.2512006)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(336065215,st_GeomFromText("POINT (7.3643371 49.2472919)"),'restaurant')
,(3428953393,st_GeomFromText("POINT (7.337102 49.3211297)"),'cafe')
,(3429083193,st_GeomFromText("POINT (7.3391121 49.3212419)"),'restaurant')
,(3696111004,st_GeomFromText("POINT (7.3389886 49.3197828)"),'restaurant')
,(3855360688,st_GeomFromText("POINT (7.3364374 49.3203047)"),'fast_food')
,(3855360690,st_GeomFromText("POINT (7.3364538 49.319683)"),'bar')
,(3855364673,st_GeomFromText("POINT (7.3390156 49.3202435)"),'fast_food')
,(3855364674,st_GeomFromText("POINT (7.3376535 49.3199005)"),'bar')
,(3908738905,st_GeomFromText("POINT (7.3421458 49.3073543)"),'cafe')
,(4033856283,st_GeomFromText("POINT (7.3386654 49.3215433)"),'cafe')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(409546150,st_GeomFromText("POINT (7.337716 49.3213583)"),'cafe')
,(4121598281,st_GeomFromText("POINT (7.3169931 49.2300542)"),'restaurant')
,(433243542,st_GeomFromText("POINT (7.360834 49.2337363)"),'cafe')
,(4382208292,st_GeomFromText("POINT (7.3371671 49.3211712)"),'restaurant')
,(4424547424,st_GeomFromText("POINT (7.3221349 49.2668185)"),'restaurant')
,(4696135619,st_GeomFromText("POINT (7.3372974 49.3180605)"),'fast_food')
,(4997469123,st_GeomFromText("POINT (7.3749026 49.1871597)"),'cafe')
,(50575704,st_GeomFromText("POINT (7.3063058 49.3167109)"),'restaurant')
,(5395641128,st_GeomFromText("POINT (7.3708716 49.2320929)"),'cafe')
,(560832889,st_GeomFromText("POINT (7.3483112 49.2934083)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(560833541,st_GeomFromText("POINT (7.3714714 49.2844285)"),'restaurant')
,(5876263457,st_GeomFromText("POINT (7.3650216 49.2448082)"),'restaurant')
,(5876263460,st_GeomFromText("POINT (7.3663776 49.2477726)"),'restaurant')
,(5876263461,st_GeomFromText("POINT (7.3650708 49.2468688)"),'restaurant')
,(588982524,st_GeomFromText("POINT (7.3406737 49.3211782)"),'fast_food')
,(590876729,st_GeomFromText("POINT (7.3386587 49.3188027)"),'restaurant')
,(590876732,st_GeomFromText("POINT (7.3411043 49.3205436)"),'restaurant')
,(590973220,st_GeomFromText("POINT (7.3417046 49.3215586)"),'restaurant')
,(599813629,st_GeomFromText("POINT (7.3392476 49.3203843)"),'restaurant')
,(599821650,st_GeomFromText("POINT (7.3402147 49.3207482)"),'cafe')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(599826199,st_GeomFromText("POINT (7.3405751 49.3214904)"),'restaurant')
,(599826200,st_GeomFromText("POINT (7.3407013 49.3215596)"),'cafe')
,(599826201,st_GeomFromText("POINT (7.3406525 49.321532)"),'fast_food')
,(599832488,st_GeomFromText("POINT (7.3402446 49.3213371)"),'pub')
,(600209623,st_GeomFromText("POINT (7.3415124 49.321442)"),'fast_food')
,(600212358,st_GeomFromText("POINT (7.3407608 49.3210348)"),'restaurant')
,(600212359,st_GeomFromText("POINT (7.3408647 49.3210955)"),'restaurant')
,(600212361,st_GeomFromText("POINT (7.3409537 49.3211404)"),'cafe')
,(633364047,st_GeomFromText("POINT (7.3979832 49.2539201)"),'pub')
,(668735691,st_GeomFromText("POINT (7.3548318 49.2319614)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(668786957,st_GeomFromText("POINT (7.3751996 49.2524877)"),'restaurant')
,(668801878,st_GeomFromText("POINT (7.3618957 49.2346484)"),'restaurant')
,(684022140,st_GeomFromText("POINT (7.3867685 49.2549649)"),'restaurant')
,(706134263,st_GeomFromText("POINT (7.3198897 49.2750818)"),'pub')
,(729600308,st_GeomFromText("POINT (7.3719837 49.2455115)"),'restaurant')
,(800603393,st_GeomFromText("POINT (7.416774 49.250944)"),'restaurant')
,(903758782,st_GeomFromText("POINT (7.3079464 49.236191)"),'pub')
,(903758807,st_GeomFromText("POINT (7.3095238 49.2370473)"),'pub')
,(933057175,st_GeomFromText("POINT (7.3612799 49.2602488)"),'restaurant')
,(937869520,st_GeomFromText("POINT (7.3599877 49.2572698)"),'restaurant')
;
INSERT INTO rateme_poi (osm_id,`position`,poi_type) VALUES 
(974251130,st_GeomFromText("POINT (7.3912026 49.2143454)"),'restaurant')
;