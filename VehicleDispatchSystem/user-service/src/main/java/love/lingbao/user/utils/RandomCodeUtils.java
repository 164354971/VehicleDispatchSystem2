package love.lingbao.user.utils;

public class RandomCodeUtils {
    public static String getSixValidationCode(){
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= 6; i++) {
            int num = (int) (Math.random() * 10);
            sb.append(num);
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String getFourValidationCode(){
        StringBuffer sb = new StringBuffer();
        String codeArr = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        for (int i = 1; i <= 4; i++) {
            int num = (int) (Math.random() * (codeArr.length()));
            sb.append(codeArr.charAt(num));
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
