import org.example.musk.utils.aes.AESKeyEnum;
import org.example.musk.utils.aes.AESUtils;

/**
 * ClassName: aesTest
 *
 * @author
 * @Description:
 * @date 2024年12月05日
 */
public class aesTest {

    public static void main(String[] args) {
        System.out.println(AESUtils.encryptHex("1" , AESKeyEnum.TENANT_KEY));
    }
}
