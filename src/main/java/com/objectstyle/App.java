package com.objectstyle;


import com.objectstyle.cayenne_demo_db.Artist;
import com.objectstyle.cayenne_demo_db.Gallery;
import com.objectstyle.cayenne_demo_db.Painting;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.apache.cayenne.query.ObjectSelect;

import java.time.LocalDate;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        ServerRuntime runtime = ServerRuntimeBuilder.builder().addConfig("cayenne-project.xml").build();
        System.out.println("Context"+runtime.newContext());
        ObjectContext context = runtime.newContext();
        /*List<Artist> artists = ObjectSelect.query(Artist.class).where(Artist.NAME.eq("Abc")).select(context);
        System.out.println("artist size "+artists.size()    );
        artists.forEach(a->{System.out.println("Artist: "+a.getName());});*/

        newObjectsTutorial(context);
        selectTutorial(context);
//        deleteTutorial(context);
    }

    static void newObjectsTutorial(ObjectContext context) {

        Artist picasso = context.newObject(Artist.class);
        picasso.setName("Pablo Picasso");
        picasso.setDateOfBirthString("18811025");

        Gallery metropolitan = context.newObject(Gallery.class);
        metropolitan.setName("Metropolitan Museum of Art");

        Painting girl = context.newObject(Painting.class);
        girl.setName("Girl Reading at a Table");

        Painting stein = context.newObject(Painting.class);
        stein.setName("Gertrude Stein");

        picasso.addToPaintings(girl);
        picasso.addToPaintings(stein);

        girl.setGallery(metropolitan);
        stein.setGallery(metropolitan);
        context.commitChanges();

    }

    static void selectTutorial(ObjectContext context) {
        List<Painting> paintings1 = ObjectSelect.query(Painting.class).select(context);

        List<Painting> paintings2 = ObjectSelect.query(Painting.class)
                .where(Painting.NAME.likeIgnoreCase("gi%")).select(context);

        List<Painting> paintings3 = ObjectSelect.query(Painting.class)
                .where(Painting.ARTIST.dot(Artist.DATE_OF_BIRTH)
                        .lt(LocalDate.of(1900,1,1))).select(context);

        paintings3.forEach(p-> System.out.println("paintining "+p.getName()+" artist "+p.getArtist()));

    }

    static void deleteTutorial(ObjectContext context) {
        // Delete object examples
        Artist picasso = ObjectSelect.query(Artist.class)
                .where(Artist.NAME.eq("Pablo Picasso")).selectOne(context);

        if (picasso != null) {
            context.deleteObjects(picasso);
            context.commitChanges();
        }
    }

}
