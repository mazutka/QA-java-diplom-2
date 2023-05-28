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
        return RandomStringUtils.randomAlphanumeric(10)+"@mail.ru";
    }

    public static String getRandomPassword(){
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String getRandomName(){
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static User getRandomUserOnlyName(){
        return new User(null,null,UserGenerator.getRandomName());
    }

    public static User getRandomUserOnlyEmail(){
        return new User(RandomStringUtils.randomAlphanumeric(10)+"@mail.ru",null,null);
    }

    public static User getRandomUserOnlyPassword(){
        return new User(null,RandomStringUtils.randomAlphanumeric(10),null);
    }

}
