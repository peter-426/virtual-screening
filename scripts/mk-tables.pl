

open(FD, $ARGV[0]) || die "$!";

while( <FD> )
{
  s/ *$//;
  s/^/<td>/;
  s/&/<\/td> <td>/g;
  #s/&/<\/td> <td><i>/;
  s/\\/<\/td><tr>/;
  s/\\//;
  
  print "$_";

}