<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MyCollab Invitation</title>
<style>
a {
  color: $styles.link_color;
}
</style>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; color: #4e4e4e; padding: 0px 0px;">
    #macro( linkBlock $webLink $displayName)
        <table style="width: auto; border-collapse: collapse; margin: 10px auto">
            <tbody>
                <tr>
                    <td>
                        <div style="border: 1px solid ${styles.border_color}; border-radius: 3px">
                            <table style="width: auto; border-collapse: collapse">
                                <tr>
                                    <td style="font: 14px/1.4285714 Arial, sans-serif; padding: 4px 10px; background-color: ${styles.action_color}">
                                        <a href="$webLink" style="color: white; text-decoration: none; font-weight: bold">$displayName</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    #end
    
    <table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
        #parse("templates/email/logo.mt")
        <tr>
            <td style="padding: 10px 30px;">
                <div>Hello $invitee.displayName, <br>
                You have a new account at <a href="$siteUrl">$siteName</a> <br>
                Account details: <br>
                Email: <a href="mail:$invitee.email">$invitee.email</a><br>
                #if ($password)
                    Password: $password
                #else
                    Password: &lt;&lt;Sent in the previous message&gt;&gt;
                #end
                </div>
                #linkBlock( $!siteUrl "Go")
            </td>
         </tr>
         #parse("templates/email/footer_en_US.mt")
    </table>
</body>
</html>