#!/usr/bin/perl -w

use POSIX ();

$|=1;

sub sigHUP_handler {
  print "got SIGHUP\n";
  $quit = 1;              # exec($SELF, @ARGV) or die "Couldn't restart: $!\n";
}


$SIG{INT} = 'sigHUP_handler';  # could fail in modules
#$SIG{INT} = \&sigHUP_handler;  # best strategy 

my $quit = 0;

code();

sub code {
  print "PID: $$\n";
  print "ARGV: @ARGV\n";
  my $c0;

  while (! $quit) {

    $c += 1;

    sleep 2;
    print "$c\n";
  }
}
__END__
