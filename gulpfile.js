var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var paths = {
    scripts: [
        'node_modules/jquery/dist/jquery.min.js'
        , 'node_modules/jsencrypt/bin/jsencrypt.js'
        , 'src/main/resources/static/js/encryption.js'
    ]
};
gulp.task('clean', function () {

});

gulp.task('default', function () {
    gulp.src(paths.scripts).pipe(uglify()).pipe(concat('main.min.js')).pipe(gulp.dest('src/main/resources/static/js'));
});