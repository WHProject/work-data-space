# $language = "VBScript"
# $interface = "1.0"

Sub Main
	crt.Session.Connect("/s 192.168.18.41")

	Set tab1 = crt.GetTab(1)
	tab1.Caption = "10.76"

	Set tab2 = tab1.Clone()
	tab2.Caption = "5.113"

	tab1.Activate

	If tab1.Session.Connected = True Then
		tab1.Screen.Send "10.76" & Chr(13)
		tab1.Screen.WaitForString "[zhangyang@"
		tab1.Screen.Send "tail -n 100 /log/web/service/ZNTG-APP_runtime_info.log" & Chr(13)
	End If


	tab2.Activate

	If tab2.Session.Connected = True Then
		tab2.Screen.Send "5.113" & Chr(13)
		tab2.Screen.WaitForString "[zhangyang@"
		tab2.Screen.Send "tail -n 100 /log/web/service/ZNTG-APP_runtime_info.log" & Chr(13)
	End If

End Sub
