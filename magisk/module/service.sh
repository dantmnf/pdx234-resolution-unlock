# query WindowManagerService for mSystemBooted=true
if [ "$(getprop ro.build.version.sdk)" = 33 ]
then
  exit 0
fi

count=0
while let "count++ < 100"
do
  dumpsys window | grep -q mSystemBooted=true && break
  sleep 1
done

rate="$(settings get global user_preferred_refresh_rate)"
height="$(settings get global user_preferred_resolution_height)"
width="$(settings get global user_preferred_resolution_width)"

cmd display set-user-preferred-display-mode "$width" "$height" "$rate"
