
wait_service() {
  while service check "$1" | grep -q 'not found'
  do
    sleep 1
  done
}

wait_service display
wait_service settings

rate="$(settings get global user_preferred_refresh_rate)"
height="$(settings get global user_preferred_resolution_height)"
width="$(settings get global user_preferred_resolution_width)"

cmd display set-user-preferred-display-mode "$width" "$height" "$rate"
