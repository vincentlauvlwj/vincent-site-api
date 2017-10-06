package me.liuwj.site.model;

import lombok.Data;

import java.io.File;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

/**
 * Created by vince on 2017/4/30.
 */
@Data
public class Comment {
    private static final String ALL_EMOJIS = "100,1234,8ball,a,ab,abc,abcd,accept,aerial-tramway,airplane,alarm-clock,alien,ambulance,anchor,angel,anger,angry,anguished,ant,apple,aquarius,aries,arrow-backward,arrow-double-down,arrow-double-up,arrow-down-small,arrow-down,arrow-forward,arrow-heading-down,arrow-heading-up,arrow-left,arrow-lower-left,arrow-lower-right,arrow-right-hook,arrow-right,arrow-up-down,arrow-up-small,arrow-up,arrow-upper-left,arrow-upper-right,arrows-clockwise,arrows-counterclockwise,art,articulated-lorry,astonished,atm,b,baby-bottle,baby-chick,baby-symbol,baby,back,baggage-claim,balloon,ballot-box-with-check,bamboo,banana,bangbang,bank,bar-chart,barber,baseball,basketball,bath,bathtub,battery,bear,bee,beer,beers,beetle,beginner,bell,bento,bicyclist,bike,bikini,bird,birthday,black-circle,black-joker,black-medium-small-square,black-medium-square,black-nib,black-small-square,black-square-button,black-square,blossom,blowfish,blue-book,blue-car,blue-heart,blush,boar,boat,bomb,book,bookmark-tabs,bookmark,books,boom,boot,bouquet,bow,bowling,bowtie,boy,bread,bride-with-veil,bridge-at-night,briefcase,broken-heart,bug,bulb,bullettrain-front,bullettrain-side,bus,busstop,bust-in-silhouette,busts-in-silhouette,cactus,cake,calendar,calling,camel,camera,cancer,candy,capital-abcd,capricorn,car,card-index,carousel-horse,cat,cat2,cd,chart-with-downwards-trend,chart-with-upwards-trend,chart,checkered-flag,cherries,cherry-blossom,chestnut,chicken,children-crossing,chocolate-bar,christmas-tree,church,cinema,circus-tent,city-sunrise,city-sunset,cl,clap,clapper,clipboard,clock1,clock10,clock1030,clock11,clock1130,clock12,clock1230,clock130,clock2,clock230,clock3,clock330,clock4,clock430,clock5,clock530,clock6,clock630,clock7,clock730,clock8,clock830,clock9,clock930,closed-book,closed-lock-with-key,closed-umbrella,cloud,clubs,cn,cocktail,coffee,cold-sweat,collision,computer,confetti-ball,confounded,confused,congratulations,construction-worker,construction,convenience-store,cookie,cool,cop,copyright,corn,couple-with-heart,couple,couplekiss,cow,cow2,credit-card,crescent-moon,crocodile,crossed-flags,crown,cry,crying-cat-face,crystal-ball,cupid,curly-loop,currency-exchange,curry,custard,customs,cyclone,dancer,dancers,dango,dart,dash,date,de,deciduous-tree,department-store,diamond-shape-with-a-dot-inside,diamonds,disappointed-relieved,disappointed,dizzy-face,dizzy,do-not-litter,dog,dog2,dollar,dolls,dolphin,donut,door,doughnut,dragon-face,dragon,dress,dromedary-camel,droplet,dvd,e-mail,ear-of-rice,ear,earth-africa,earth-americas,earth-asia,egg,eggplant,eight-pointed-black-star,eight-spoked-asterisk,eight,electric-plug,elephant,email,end,envelope,es,euro,european-castle,european-post-office,evergreen-tree,exclamation,expressionless,eyeglasses,eyes,facepunch,factory,fallen-leaf,family,fast-forward,fax,fearful,feelsgood,feet,ferris-wheel,file-folder,finnadie,fire-engine,fire,fireworks,first-quarter-moon-with-face,first-quarter-moon,fish-cake,fish,fishing-pole-and-fish,fist,five,flags,flashlight,floppy-disk,flower-playing-cards,flushed,foggy,football,fork-and-knife,fountain,four-leaf-clover,four,fr,free,fried-shrimp,fries,frog,frowning,fu,fuelpump,full-moon-with-face,full-moon,game-die,gb,gem,gemini,ghost,gift-heart,gift,girl,globe-with-meridians,goat,goberserk,godmode,golf,grapes,green-apple,green-book,green-heart,grey-exclamation,grey-question,grimacing,grin,grinning,guardsman,guitar,gun,haircut,hamburger,hammer,hamster,hand,handbag,hankey,hash,hatched-chick,hatching-chick,headphones,hear-no-evil,heart-decoration,heart-eyes-cat,heart-eyes,heart,heartbeat,heartpulse,hearts,heavy-check-mark,heavy-division-sign,heavy-dollar-sign,heavy-exclamation-mark,heavy-minus-sign,heavy-multiplication-x,heavy-plus-sign,helicopter,herb,hibiscus,high-brightness,high-heel,hocho,honey-pot,honeybee,horse-racing,horse,hospital,hotel,hotsprings,hourglass-flowing-sand,hourglass,house-with-garden,house,hurtrealbad,hushed,ice-cream,icecream,id,ideograph-advantage,imp,inbox-tray,incoming-envelope,information-desk-person,information-source,innocent,interrobang,iphone,it,izakaya-lantern,jack-o-lantern,japan,japanese-castle,japanese-goblin,japanese-ogre,jeans,joy-cat,joy,jp,key,keycap-ten,kimono,kiss,kissing-cat,kissing-closed-eyes,kissing-face,kissing-heart,kissing-smiling-eyes,kissing,koala,koko,kr,large-blue-circle,large-blue-diamond,large-orange-diamond,last-quarter-moon-with-face,last-quarter-moon,laughing,leaves,ledger,left-luggage,left-right-arrow,leftwards-arrow-with-hook,lemon,leo,leopard,libra,light-rail,link,lips,lipstick,lock-with-ink-pen,lock,lollipop,loop,loudspeaker,love-hotel,love-letter,low-brightness,m,mag-right,mag,mahjong,mailbox-closed,mailbox-with-mail,mailbox-with-no-mail,mailbox,man-with-gua-pi-mao,man-with-turban,man,mans-shoe,maple-leaf,mask,massage,meat-on-bone,mega,melon,memo,mens,metal,metro,microphone,microscope,milky-way,minibus,minidisc,minus1,mobile-phone-off,money-with-wings,moneybag,monkey-face,monkey,monorail,mortar-board,mount-fuji,mountain-bicyclist,mountain-cableway,mountain-railway,mouse,mouse2,movie-camera,moyai,muscle,mushroom,musical-keyboard,musical-note,musical-score,mute,nail-care,name-badge,neckbeard,necktie,negative-squared-cross-mark,neutral-face,new-moon-with-face,new-moon,new,newspaper,ng,nine,no-bell,no-bicycles,no-entry-sign,no-entry,no-good,no-mobile-phones,no-mouth,no-pedestrians,no-smoking,non-potable-water,nose,notebook-with-decorative-cover,notebook,notes,nut-and-bolt,o,o2,ocean,octocat,octopus,oden,office,ok-hand,ok-woman,ok,older-man,older-woman,on,oncoming-automobile,oncoming-bus,oncoming-police-car,oncoming-taxi,one,open-file-folder,open-hands,open-mouth,ophiuchus,orange-book,outbox-tray,ox,package,page-facing-up,page-with-curl,pager,palm-tree,panda-face,paperclip,parking,part-alternation-mark,partly-sunny,passport-control,paw-prints,peach,pear,pencil,pencil2,penguin,pensive,performing-arts,persevere,person-frowning,person-with-blond-hair,person-with-pouting-face,phone,pig-nose,pig,pig2,pill,pineapple,pisces,pizza,plus1,point-down,point-left,point-right,point-up-2,point-up,police-car,poodle,poop,post-office,postal-horn,postbox,potable-water,pouch,poultry-leg,pound,pouting-cat,pray,princess,punch,purple-heart,purse,pushpin,put-litter-in-its-place,question,rabbit,rabbit2,racehorse,radio-button,radio,rage,rage1,rage2,rage3,rage4,railway-car,rainbow,raised-hand,raised-hands,raising-hand,ram,ramen,rat,recycle,red-car,red-circle,registered,relaxed,relieved,repeat-one,repeat,restroom,revolving-hearts,rewind,ribbon,rice-ball,rice-cracker,rice-scene,rice,ring,rocket,roller-coaster,rooster,rose,rotating-light,round-pushpin,rowboat,ru,rugby-football,runner,running-shirt-with-sash,running,sa,sagittarius,sailboat,sake,sandal,santa,satellite,satisfied,saxophone,school-satchel,school,scissors,scorpius,scream-cat,scream,scroll,seat,secret,see-no-evil,seedling,seven,shaved-ice,sheep,shell,ship,shipit,shirt,shit,shoe,shower,signal-strength,six-pointed-star,six,ski,skull,sleeping,sleepy,slot-machine,small-blue-diamond,small-orange-diamond,small-red-triangle-down,small-red-triangle,smile-cat,smile,smiley-cat,smiley,smiling-imp,smirk-cat,smirk,smoking,snail,snake,snowboarder,snowflake,snowman,sob,soccer,soon,sos,sound,space-invader,spades,spaghetti,sparkle,sparkler,sparkles,sparkling-heart,speak-no-evil,speaker,speech-balloon,speedboat,squirrel,star,star2,stars,station,statue-of-liberty,steam-locomotive,stew,straight-ruler,strawberry,stuck-out-tongue-closed-eyes,stuck-out-tongue-winking-eye,stuck-out-tongue,sun-with-face,sunflower,sunglasses,sunny,sunrise-over-mountains,sunrise,surfer,sushi,suspect,suspension-railway,sweat-drops,sweat-smile,sweat,sweet-potato,swimmer,symbols,syringe,tada,tanabata-tree,tangerine,taurus,taxi,tea,telephone-receiver,telephone,telescope,tennis,tent,thought-balloon,three,thumbsdown,thumbsup,ticket,tiger,tiger2,tired-face,tm,toilet,tokyo-tower,tomato,tongue,top,tophat,tractor,traffic-light,train,train2,tram,triangular-flag-on-post,triangular-ruler,trident,triumph,trolleybus,trollface,trophy,tropical-drink,tropical-fish,truck,trumpet,tshirt,tulip,turtle,tv,twisted-rightwards-arrows,two-hearts,two-men-holding-hands,two-women-holding-hands,two,u5272,u5408,u55b6,u6307,u6708,u6709,u6e80,u7121,u7533,u7981,u7a7a,uk,umbrella,unamused,underage,unlock,up,us,v,vertical-traffic-light,vhs,vibration-mode,video-camera,video-game,violin,virgo,volcano,vs,walking,waning-crescent-moon,waning-gibbous-moon,warning,watch,water-buffalo,watermelon,wave,wavy-dash,waxing-crescent-moon,waxing-gibbous-moon,wc,weary,wedding,whale,whale2,wheelchair,white-check-mark,white-circle,white-flower,white-large-square,white-medium-small-square,white-medium-square,white-small-square,white-square-button,wind-chime,wine-glass,wink,wolf,woman,womans-clothes,womans-hat,womens,worried,wrench,x,yellow-heart,yen,yum,zap,zero,zzz";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
    private static final Pattern EMOJI_PATTERN = Pattern.compile("\\[([^\\[\\]]+)\\]");

    private int id;
    private String pageId;
    private User fromUser;
    private User toUser;
    private String content;
    private Timestamp createDate;

    public String getUrl() {
        return "https://www.liuwj.me" + pageId + "#comment-" + id;
    }

    public String getCreateDateStr() {
        return createDate.toLocalDateTime().format(FORMATTER);
    }

    public Comment toFront() {
        Comment comment = new Comment();
        comment.id = id;
        comment.pageId = pageId;
        comment.fromUser = fromUser.toFront();
        comment.toUser = toUser == null ? null : toUser.toFront();
        comment.content = handleEmoji(content);
        comment.createDate = new Timestamp(createDate.getTime());
        return comment;
    }

    private static String handleEmoji(String content) {
        Matcher matcher = EMOJI_PATTERN.matcher(content);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String target = matcher.group(1);
            String replacement;
            if (ALL_EMOJIS.contains(target)) {
                replacement = "<i class='emoji emoji-" + target + "'></i>";
            } else {
                replacement = "[" + target + "]";
            }
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static void main(String[] args) {
        File dir = new File("D:\\GitRepositories\\vincent-site-web\\themes\\next\\source\\css\\images");
        String[] fileNames = dir.list();
        assert fileNames != null;

        for (String name : fileNames) {
            System.out.println(name);
        }
        System.out.println("total: " + fileNames.length);

        System.out.println(Arrays.stream(fileNames).map(s -> s.replace(".png", "")).collect(joining(",")));

        Arrays.stream(fileNames)
                .map(s -> s.replace(".png", ""))
                .map(s -> ".emoji-" + s + "{background-image:url('images/" + s + ".png');}")
                .forEach(System.out::print);
    }
}
