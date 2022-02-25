 

foreach $ad (@ARGV)
{
  print "condor_submit $ad [Y/N]: ";
  
#  $r = <STDIN>;
#  if ( $r =~ /y/i ) {

  print " >> submitting ... \n ";

  `condor_submit $ad`;

#  }
}
