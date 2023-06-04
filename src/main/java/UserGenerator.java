import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User getRandomUser(){
        return new User(UserGenerator.getRandomEmail(),UserGenerator.getRandomPassword(),UserGenerator.getRandomName());
    }

    public static User getRandomWithoutEmail(){
        return new User(null,UserGenerator.getRandomPassword(),UserGenerator.getRandomName());
    }

    public static User getRandomWithoutPassword(){
        return new User(UserGenerator.getRandomEmail(),null,UserGenerator.getRandomName());
    }

    public static User getRandomWithoutName(){
        return new User(UserGenerator.getRandomEmail(),UserGenerator.getRandomPassword(),null);
    }

    public static String getRandomEmail(){
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword(){
        Faker faker = new Faker();
        return faker.internet().password(8,16,true,true,true);
    }

    public static String getRandomName(){
        Faker faker = new Faker();
        return faker.address().firstName();
    }

    public static User getRandomUserOnlyName(){
        return new User(null,null,UserGenerator.getRandomName());
    }

    public static User getRandomUserOnlyEmail(){
        return new User(UserGenerator.getRandomEmail(),null,null);
    }

    public static User getRandomUserOnlyPassword(){
        return new User(null,UserGenerator.getRandomPassword(),null);
    }

}
