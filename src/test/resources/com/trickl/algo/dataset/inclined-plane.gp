set  autoscale                        # scale axes automatically
unset log                              # remove any log-scaling
unset label                            # remove any previous labels
set xtic auto                          # set xtics automatically
set ytic auto                          # set ytics automatically
set title "Inclined Plane Data"
splot "inclined-plane.dat" every ::1
pause -1 "\nPush 'q' and 'return' to exit Gnuplot ...\n"
